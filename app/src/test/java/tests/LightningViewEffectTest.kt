package tests

import com.henriquemachine.lightningmonitornetwork.presentation.lightning.LightningNodeViewEffect
import org.junit.Assert.assertEquals
import org.junit.Test

class LightningNodeViewEffectTest {

    @Test
    fun `test show error message effect`() {
        val errorMessage = "An error occurred"
        val effect = LightningNodeViewEffect.ShowErrorMessage(message = errorMessage)

        assertEquals(LightningNodeViewEffect.ShowErrorMessage(message = errorMessage), effect)
    }

    @Test
    fun `test show success message effect`() {
        val successMessage = "Operation successful"
        val effect = LightningNodeViewEffect.ShowSuccessMessage(message = successMessage)

        assertEquals(LightningNodeViewEffect.ShowSuccessMessage(message = successMessage), effect)
    }

    @Test
    fun `test refresh complete effect`() {
        val effect = LightningNodeViewEffect.RefreshComplete

        assertEquals(LightningNodeViewEffect.RefreshComplete, effect)
    }
}
