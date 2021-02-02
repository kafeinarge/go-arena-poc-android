package com.turkcell.turkcellsaha.ui.postdetail

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.data.model.WallContent
import com.turkcell.turkcellsaha.databinding.FragmentContentDetailBinding
import com.turkcell.turkcellsaha.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WallContentDetailFragment : BaseFragment<FragmentContentDetailBinding>() {

    override val layoutRes: Int = R.layout.fragment_content_detail


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content = arguments?.getSerializable("content") as WallContent?

        if (content?.preview.isNullOrEmpty()) {
            binding.ivContent.visibility = View.GONE
        } else {
            Glide.with(binding.ivContent.context)
                .load(Base64.decode(content?.preview, Base64.DEFAULT))
                .into(binding.ivContent)
        }

        binding.tvContent.text = content?.text

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    companion object {

        const val KEY_CONTENT = "content"

        fun newInstance(content: WallContent): WallContentDetailFragment {
            val fragment = WallContentDetailFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(KEY_CONTENT, content)
            }
            return fragment
        }
    }


}