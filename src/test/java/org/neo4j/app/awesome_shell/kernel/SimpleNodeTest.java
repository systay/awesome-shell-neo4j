package org.neo4j.app.awesome_shell.kernel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.kernel.ImpermanentGraphDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.neo4j.app.matchers.HasRelationshipNamed.hasRelationshipNamed;

public class SimpleNodeTest
{
    public SimpleGraphDatabase db;
    public SimpleNode node1;
    public SimpleNode node2;
    public SimpleNode node3;

    @Test
    public void shouldBeAbleToCreateRelationshipWithString()
    {
        node1.createRelationshipTo( node2, "friend" );

        assertThat( node1, hasRelationshipNamed( "friend" ) );
    }

    @Test
    public void shouldRemoveAllRelationshipsWhenDeletingNode()
    {
        node1.createRelationshipTo( node2, "friend" );

        node1.delete(); //Should not throw an exception
    }

    @Test( expected = RelationshipTypeWithDifferentCaseAlreadyExistsException.class )
    public void shouldThrowExceptionIfRelationshipTypeSpelledWithDifferentCaseAlreadyExists()
    {
        node1.createRelationshipTo( node2, "friend" );

        SimpleNode node3 = db.createNode();

        node1.createRelationshipTo( node3, "FRIEND" );
    }

    @Test
    public void shouldNotThrowExceptionIfRelationshipTypeSpelledWithDifferentCaseAlreadyExists()
    {
        node1.createRelationshipTo( node2, "friend" );

        SimpleNode node3 = db.createNode();

        node1.reallyCreateRelationshipTo( node3, "FRIEND" );

        assertThat( node3, hasRelationshipNamed( "FRIEND" ) );
    }

    @Test
    public void shouldBeAbleToCreateANodeWithProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put( "name", "Smith" );
        map.put( "age", 32 );
        node1 = db.createNode( map );

        assertThat( (String)node1.getProperty( "name" ), is( "Smith" ) );
    }

    @Test
    public void shouldBeAbleToCreateRelationshipsWithProperties()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put( "added", "2010-10-01" );
        node1.createRelationshipTo( node2, "friend", map );

        SimpleRelationship relationship = node1.getSingleRelationship( "friend" );

        assertThat( (String)relationship.getProperty( "added" ), is( "2010-10-01" ) );
    }

    @Test
    public void shouldBeAbleToFollowALinkedList()
    {
        node1.createRelationshipTo( node2, "next" );

        node1.followLinkedList( "next", Direction.OUTGOING );
    }

    @Test
    public void linkedListWithOnlyOneElementWorks()
    {
        SimpleNode result = node1.followLinkedList( "next", Direction.OUTGOING );

        assertThat( result, is( node1 ) );
    }

    @Test
    public void linkedListWithRelationshipOnTheWrongDirectionIsNotFollowed()
    {
        node2.createRelationshipTo( node1, "next" );

        SimpleNode result = node1.followLinkedList( "next", Direction.OUTGOING );

        assertThat( result, is( node1 ) );
    }

    @Test
    public void canSetMultiplePropertiesAtOnce()
    {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put( "monkeys", "rock" );
        map.put( "hello", "world" );

        node1.setProperties( map );

        assertThat( (String)node1.getProperty( "monkeys" ), is( "rock" ) );
        assertThat( (String)node1.getProperty( "hello" ), is( "world" ) );
    }

    @Test
    public void singleRelationshipShouldNotThrowExceptionIfNothingFound()
    {
        SimpleRelationship rel = node1.getSingleRelationship( "friend" );
        assertThat( rel, is( nullValue() ) );
    }

    @Test
    public void shouldNotWarnAboutMultipleRelationshipTypesIfTheyHaveTheSameCase()
    {
        node1.createRelationshipTo( node2, "friend" );
        node1.createRelationshipTo( node3, "friend" );
    }

    @Test
    public void shouldBePossibleToFindSingleShortestPathBetweenNodes()
    {
        node1.createRelationshipTo( node2, "friend" );
        node2.createRelationshipTo( node3, "friend" );

        //node1.findSingleShortestPathTo(node3);
    }

    @Test
    public void shouldBeAbleToIndexANode() throws Exception
    {
        node1.setProperty( "name", "Andres Taylor" );
        db.nodeIndex( "names" ).add( node1, "name" );
        SimpleNode result = db.nodeIndex( "names" ).getSingle( "name", "Andres Taylor" );

        assertThat( result, not( nullValue() ) );
        assertThat( result, is( node1 ) );
    }


    @Test(expected = NoSuchElementException.class)
    public void shouldThrowExceptionIfNoNodeFound() throws Exception
    {
        db.nodeIndex( "names" ).getSingle( "foo", "bar" );
    }

    @Before
    public void setUp() throws Exception
    {
        db = new SimpleGraphDatabase( new ImpermanentGraphDatabase( "neodb" ) );
        node1 = db.createNode();
        node2 = db.createNode();
        node3 = db.createNode();
    }

    @After
    public void tearDown() throws Exception
    {
        db.shutdown();
    }
}
