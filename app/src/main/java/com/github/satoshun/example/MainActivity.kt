package com.github.satoshun.example

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.github.satoshun.example.databinding.MainActBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  private lateinit var binding: MainActBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
      View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
      View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.main_act)

    val params = binding.bottom.layoutParams as CoordinatorLayout.LayoutParams
    val behavior = params.behavior as BottomSheetBehavior<View>
    behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onSlide(bottomSheet: View, slideOffset: Float) {
        println("onSlide $slideOffset")

        val s = if (slideOffset < 0) -slideOffset else slideOffset

        binding.bottomButton.translationY = -(
          (bottomSheet.height - behavior.peekHeight) * (1.0 - s))
          .toFloat()

        binding.backBackground.alpha = slideOffset
      }

      override fun onStateChanged(bottomSheet: View, newState: Int) {
        println("onStateChanged $newState")
        if (newState == BottomSheetBehavior.STATE_SETTLING) {
          return
        }

        binding.backBackground.isVisible =
          newState != BottomSheetBehavior.STATE_COLLAPSED
      }
    })

    binding.title1.setOnClickListener {
      if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return@setOnClickListener
      }
      if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return@setOnClickListener
      }
    }

    binding.title2.setOnClickListener {
      binding.goneable.isVisible = !binding.goneable.isVisible
      if (binding.goneable.isVisible) {
        lifecycleScope.launch {
          delay(16)
          behavior.setPeekHeight(dpToPx(120 + 100), true)
        }
      } else {
        lifecycleScope.launch {
          delay(16)
          behavior.setPeekHeight(dpToPx(120), true)
        }
      }
    }

    binding.backBackground.setOnClickListener {
      behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    binding.bottomContainer.setOnApplyWindowInsetsListener { v, insets ->
      println(insets.systemWindowInsetBottom.toFloat())
      v.translationY = -insets.systemWindowInsetBottom.toFloat()
      insets
    }
  }
}

@Px
private fun Context.dpToPx(
  value: Int
): Int = (value * this.resources.displayMetrics.density).toInt()
