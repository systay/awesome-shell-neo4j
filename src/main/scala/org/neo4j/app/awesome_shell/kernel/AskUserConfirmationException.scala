package org.neo4j.app.awesome_shell.kernel

class AskUserConfirmationException(
                                    failureMessage: String,
                                    confirmationQuestion: String,
                                    userConfirmed: () => Unit) extends RuntimeException(failureMessage)
{
  def getConfirmationQuestion = confirmationQuestion

  def userHasConfirmed = userConfirmed.apply
}