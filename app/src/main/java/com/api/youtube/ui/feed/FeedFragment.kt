package com.api.youtube.ui.feed

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.api.youtube.databinding.FragmentFeedBinding
import com.api.youtube.ui.feed.adapter.FeedsAdapter
import com.api.youtube.utils.hideKeyboard

class FeedFragment : Fragment() {
    
    private val binding by lazy { FragmentFeedBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(requireActivity())[FeedViewModel::class.java] }
    private val adapter by lazy {
        FeedsAdapter(
            { viewModel.hasNextPage() },
            { viewModel.downloadNextPage() },
            { Toast.makeText(requireContext(), it.snippet?.title, Toast.LENGTH_SHORT).show() }
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        
        binding.editSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
    
            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.imageClear.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
    
            override fun afterTextChanged(p0: Editable?) = Unit
        })
    
        binding.layoutSwipeRefresh.setOnRefreshListener { viewModel.reload() }
        binding.listSearchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.listSearchResult.adapter = adapter
    
        binding.editSearch.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH) {
                viewModel.search(binding.editSearch.text.toString())
                hideKeyboard(binding.editSearch)
            }
            false
        }
    
        binding.imageClear.setOnClickListener {
            binding.editSearch.text.clear()
            viewModel.reload()
        }

        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        viewModel.searchResult.observe(viewLifecycleOwner) {
            adapter.update(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.layoutSwipeRefresh.isRefreshing = it
        }

        viewModel.reload()
    }
}