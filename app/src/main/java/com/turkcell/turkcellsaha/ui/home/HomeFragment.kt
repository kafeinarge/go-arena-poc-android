package com.turkcell.turkcellsaha.ui.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.data.model.Wall
import com.turkcell.turkcellsaha.data.model.WallContent
import com.turkcell.turkcellsaha.databinding.FragmentHomeBinding
import com.turkcell.turkcellsaha.ui.base.BaseFragment
import com.turkcell.turkcellsaha.ui.home.adapter.WallContentRvAdapter
import com.turkcell.turkcellsaha.ui.newpost.NewPostFragment
import com.turkcell.turkcellsaha.ui.postdetail.WallContentDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val layoutRes: Int = R.layout.fragment_home

    private val viewModel: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchWallContent()

        viewModel.wallContentLiveData_.observe(viewLifecycleOwner, {
            disableSwipeRefresh()
            initWall(it)
        })

        viewModel.status_.observe(viewLifecycleOwner, {
            disableSwipeRefresh()
            if (it.isLoading()) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }

            if (it.shouldShowErrorMessage()) {
                Toast.makeText(requireContext(), it.getErrorMessage(), Toast.LENGTH_LONG).show()
            }
        })

        initClickListeners()
        initSwipeRefresh()
    }

    private fun initClickListeners() {
        binding.ivChart.setOnClickListener {
            findNavController().navigate(R.id.fragment_statistics)
        }

        binding.ivNewPost.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_home_to_fragment_new_post,
                Bundle().apply {
                    putInt(NewPostFragment.KEY_MODE, NewPostFragment.KEY_VIEW_MODE)
                })
        }
    }

    private fun initWall(wall: Wall) {
        binding.rvWall.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        val adapter = WallContentRvAdapter().apply {
            onItemClickListener = {
                findNavController().navigate(
                    R.id.action_navigation_home_to_fragment_content_detail,
                    Bundle().apply {
                        putSerializable("content", it)
                    })
            }

            onMoreClickListener = {
                showOperations(it)
            }
        }
        binding.rvWall.adapter = adapter
        adapter.appendList(wall.contents)

    }

    private fun initSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchWallContent()
        }
    }

    private fun disableSwipeRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showOperations(content: WallContent) {
        showOperationsDialog(BottomSheet(LayoutMode.WRAP_CONTENT), content)
    }

    private fun showOperationsDialog(
        dialogBehavior: DialogBehavior = ModalDialog,
        content: WallContent
    ) {
        val dialog = MaterialDialog(requireContext(), dialogBehavior).show {
            title(R.string.title_operation)
            customView(
                R.layout.custom_bottom_operations,
                scrollable = true,
                horizontalPadding = true
            )
        }

        val customView = dialog.getCustomView()
        val tvDetail = customView.findViewById<TextView>(R.id.tvDetail)
        val tvEdit = customView.findViewById<TextView>(R.id.tvEdit)
        val tvDelete = customView.findViewById<TextView>(R.id.tvDelete)

        tvDetail.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(
                R.id.action_navigation_home_to_fragment_content_detail,
                Bundle().apply {
                    putSerializable("content", content)
                })
        }

        tvEdit.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(
                R.id.action_navigation_home_to_fragment_new_post,
                Bundle().apply {
                    putSerializable(NewPostFragment.KEY_CONTENT, content)
                    putInt(NewPostFragment.KEY_MODE, NewPostFragment.KEY_EDIT_MODE)
                })
        }

        tvDelete.setOnClickListener {
            dialog.dismiss()
            viewModel.deletePost(content.id)
        }
    }
}