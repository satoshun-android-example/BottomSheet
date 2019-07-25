package com.github.satoshun.example

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.github.satoshun.example.databinding.MainActBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

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
  }
}
