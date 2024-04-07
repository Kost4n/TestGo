package com.kost4n.testgo.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.kost4n.testgo.R
import com.kost4n.testgo.adapter.CategoryAdapter
import com.kost4n.testgo.adapter.MealsAdapter
import com.kost4n.testgo.adapter.RecyclerViewDecoration
import com.kost4n.testgo.databinding.FragmentMenuBinding
import com.kost4n.testgo.model.Category
import com.kost4n.testgo.viewmodels.MenuViewModel


class MenuFragment: Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuViewModel: MenuViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var mealsAdapter: MealsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        _binding = FragmentMenuBinding.inflate(layoutInflater, container, false)

        menuViewModel.getCategories()
        menuViewModel.getMeals()

        binding.categories.setHasFixedSize(true)
        binding.categories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        menuViewModel.categoryList.observe(viewLifecycleOwner) {
            val mealsClickListener = object : CategoryAdapter.OnRecordClickListener {
                override fun onClick(category: Category, view: View) {
                    Log.d("MF", "click on category")
                    menuViewModel.updateMealsByCategory(category.strCategory!!)
//                    setNormalColor()
//                    view.setTextColor(Color.parseColor("#FD3A69"))
//                    view.setBackgroundColor(Color.parseColor("#f3ccd6"))
                }
            }
            categoryAdapter = CategoryAdapter(it, mealsClickListener)
            categoryAdapter.notifyDataSetChanged()
            binding.categories.adapter = categoryAdapter
        }

        binding.meals.setHasFixedSize(true)
        binding.meals.layoutManager = LinearLayoutManager(context)
        binding.meals.addItemDecoration(RecyclerViewDecoration(8))

        menuViewModel.mealList.observe(viewLifecycleOwner) {
            mealsAdapter = MealsAdapter(it, requireActivity())
            mealsAdapter.notifyDataSetChanged()
            binding.meals.adapter = mealsAdapter
        }

        var isShow = true
        var scrollRange = -1
        binding.bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
                binding.banners.visibility = View.VISIBLE
            }
            if (scrollRange + verticalOffset == 0) {
                binding.bar.setBackgroundColor(Color.parseColor("#FD3A69"))
                binding.collapsingBar.title = resources.getString(R.string.title_menu)
                isShow = true
                binding.banners.visibility = View.INVISIBLE
            } else if (isShow){
                binding.collapsingBar.title = " " //careful there should a space between double quote otherwise it wont work
                isShow = false
                binding.bar.setBackgroundColor(Color.parseColor("#ffffff"))
                binding.banners.visibility = View.VISIBLE
            }
        })


        return binding.root
    }

    fun setNormalColor() {
        binding.categories.children.forEach { view ->
//            val itemView = view
//            itemView.setTextColor(Color.parseColor("#757575"))
            view.setBackgroundColor(Color.parseColor("#ffffff"))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}