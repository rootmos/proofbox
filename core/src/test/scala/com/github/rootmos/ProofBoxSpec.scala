package com.github.rootmos

import shapeless.test.illTyped
import org.scalatest._

class ProofBoxSpec extends WordSpec with Matchers with GivenWhenThen {

  "ProofBox" should {
    "capture implicit from a given context" in {
      Given("two proofs in a trait containing them")
      trait AProof
      trait AnotherProof
      trait MyContext {
        implicit val aProof: AProof = new AProof {}
        implicit val anotherProof: AnotherProof = new AnotherProof {}
      }
      object MyContext extends MyContext

      Then("without the proofs nothing can be proven")
      illTyped { """implicitly[AProof]""" }
      illTyped { """implicitly[AnotherProof]""" }

      When("the proof is captured using the box")
      implicit val localProof = ProofBox[MyContext, AProof]

      Then("the proof can be found")
      implicitly[AProof] shouldBe a [AProof]

      And("the second proof remains not in scope")
      illTyped { """implicitly[AnotherProof]""" }
    }
  }
}
