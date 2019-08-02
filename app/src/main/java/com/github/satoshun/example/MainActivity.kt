package com.github.satoshun.example

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.main_act)

    val params = binding.bottom.layoutParams as CoordinatorLayout.LayoutParams
    val behavior = params.behavior as BottomSheetBehavior<View>
    behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onSlide(bottomSheet: View, slideOffset: Float) {
        println("onSlide $slideOffset")

        println((bottomSheet.height * (1.0 - slideOffset)).toFloat())

        binding.bottomButton.translationY = -(
          (bottomSheet.height - behavior.peekHeight) * (1.0 - slideOffset))
          .toFloat()
      }

      override fun onStateChanged(bottomSheet: View, newState: Int) {
        println("onStateChanged $newState")
      }
    })

    binding.title1.setOnClickListener {
      if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        return@setOnClickListener
      }
      if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
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

//    val transition = LayoutTransition()
//    transition.setAnimateParentHierarchy(false)
//    binding.bottom.layoutTransition = transition
  }
}


@Px
private fun Context.dpToPx(
  value: Int
): Int = (value * this.resources.displayMetrics.density).toInt()
