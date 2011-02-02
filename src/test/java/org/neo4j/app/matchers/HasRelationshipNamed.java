package org.neo4j.app.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.neo4j.app.awesome_shell.kernel.SimpleNode;
import org.neo4j.app.awesome_shell.kernel.SimpleRelationship;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.ArrayList;
import java.util.List;

public class HasRelationshipNamed extends TypeSafeMatcher<SimpleNode>
{
    private String name;
    private String errorMessage;

    public HasRelationshipNamed( String name )
    {
        this.name = name;
    }

    public void describeTo( Description description )
    {
        description.appendText( errorMessage );
    }

    @Override
    public boolean matchesSafely( SimpleNode simpleNode )
    {
        StringBuilder builder = new StringBuilder( );
        List<SimpleRelationship> relationships = new ArrayList<SimpleRelationship>();
        IteratorUtil.addToCollection( simpleNode.getRelationships().iterator(), relationships );
        for ( SimpleRelationship rel : relationships )
        {
            String typeName = rel.getTypeName();
            if ( typeName.equals( name ) )
            {
                return true;
            }

            if(builder.length() > 0)
            {
                builder.append( ", " );
            }
            builder.append( typeName );
        }

        errorMessage = " at least one relationship named " + name + ", but got relationships named: " + builder.toString();
        return false;
    }

    @Factory
    public static HasRelationshipNamed hasRelationshipNamed( String name)
    {
        return new HasRelationshipNamed( name );
    }
}
