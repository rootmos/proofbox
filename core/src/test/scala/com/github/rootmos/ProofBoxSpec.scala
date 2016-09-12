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

      Then("without the proofs nothing can be proven")
      illTyped { """implicitly[AProof]""" }
      illTyped { """implicitly[AnotherProof]""" }

      When("the proof is captured using the box")
      // TODO: make it possible to use a type annotation, currently gives you ambiguous
      // instances because the macro captures the outer context...
      implicit val localProof = ProofBox[MyContext, AProof]

      Then("the proof can be found")
      implicitly[AProof] shouldBe a [AProof]

      And("the second proof is still not in scope")
      illTyped { """implicitly[AnotherProof]""" }
    }

    "capture implicit from two contexts" in {
      Given("a proof that depends on another")
      trait FirstProof
      trait FirstContext {
        implicit val firstProof: FirstProof = new FirstProof {}
      }

      trait DependantProof
      trait DependantContext {
        implicit def dependantProof(implicit a: FirstProof): DependantProof = new DependantProof {}
      }

      Then("without the proofs nothing can be proven")
      illTyped { """implicitly[FirstProof]""" }
      illTyped { """implicitly[DependantProof]""" }

      When("the dependant proof is captured using the box")
      // TODO: This work with: ProofBox[FirstContext with DependantContex, DependantProof] directly, now
      // this complains about RefinedTypes...
      trait T extends FirstContext with DependantContext
      implicit val localProof = ProofBox[T, DependantProof]

      Then("the proof can be found")
      implicitly[DependantProof] shouldBe a [DependantProof]

      And("the prerequisite proof is still not in scope")
      illTyped { """implicitly[FirstProof]""" }
    }
  }
}
