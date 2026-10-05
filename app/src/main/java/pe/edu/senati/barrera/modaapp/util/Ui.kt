package pe.edu.senati.barrera.modaapp.util

import android.app.Activity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

object Ui {
    fun ajustar(activity: Activity, root: View, header: View? = null) {
        WindowCompat.getInsetsController(activity.window, root)
            .isAppearanceLightStatusBars = (header == null)

        ViewCompat.setOnApplyWindowInsetsListener(root) { _, insets ->
            val s = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            root.updatePadding(
                left = s.left, right = s.right, bottom = s.bottom,
                top = if (header == null) s.top else 0
            )
            header?.updatePadding(top = s.top)
            insets
        }
    }
}