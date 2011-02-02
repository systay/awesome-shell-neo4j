package org.neo4j.app.awesome_shell.kernel

class RelationshipTypeWithDifferentCaseAlreadyExistsException(
                                                               existingRelationshipType: String,
                                                               newTypeName: String,
                                                               userConfirmed: () => Unit)
  extends AskUserConfirmationException(
    failureMessage = "A relationship type named [" + existingRelationshipType + "] already exists.",
    confirmationQuestion = "Do you want to create a new relationship type named [" + newTypeName + "]?",
    userConfirmed = userConfirmed
  )