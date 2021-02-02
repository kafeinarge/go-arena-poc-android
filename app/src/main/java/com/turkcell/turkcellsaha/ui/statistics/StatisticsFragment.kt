package com.turkcell.turkcellsaha.ui.statistics

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.data.model.SummaryContent
import com.turkcell.turkcellsaha.data.model.Summary
import com.turkcell.turkcellsaha.databinding.FragmentStatisticsBinding
import com.turkcell.turkcellsaha.ui.base.BaseFragment
import com.turkcell.turkcellsaha.ui.statistics.adapter.CategoryRvAdapter
import com.turkcell.turkcellsaha.ui.statistics.adapter.MonthsRvAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    override val layoutRes: Int = R.layout.fragment_statistics

    private val viewModel: StatisticsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchSummaries(1)

        viewModel.summaryLiveData_.observe(viewLifecycleOwner, {
            initSummaryChart(it)
        })

        viewModel.status_.observe(viewLifecycleOwner, {
            if (it.isLoading()) {
                binding.loading.visibility = View.VISIBLE
            } else {
                binding.loading.visibility = View.GONE
            }

            if (it.shouldShowErrorMessage()) {
                Toast.makeText(requireContext(), it.getErrorMessage(), Toast.LENGTH_LONG).show()
            }
        })

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.ivFilter.setOnClickListener {
            showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        }
    }

    private fun initSummaryChart(summary: Summary) {
        val categoryAdapter = CategoryRvAdapter()
        val xAxis: XAxis = binding.chart.xAxis
        xAxis.setDrawGridLines(false)

        binding.chart.axisLeft.setDrawGridLines(false)
        binding.chart.axisRight.setDrawGridLines(false)
        binding.chart.axisRight.isEnabled = false
        binding.chart.axisLeft.isEnabled = true
        binding.chart.xAxis.setDrawGridLines(false)
        binding.chart.animateY(1000)

        binding.chart.legend.isEnabled = false;

        binding.chart.axisRight.setDrawLabels(false)
        binding.chart.axisLeft.setDrawLabels(true)
        binding.chart.setTouchEnabled(false);
        binding.chart.isDoubleTapToZoomEnabled = false
        binding.chart.xAxis.isEnabled = true;
        binding.chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart.invalidate()


        binding.rvCategoryData.adapter = categoryAdapter
        binding.rvCategoryData.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        categoryAdapter.appendList(summary.contents)
        createBarChart(summary.contents)
    }

    private fun createBarChart(dataList: List<SummaryContent>) {
        val values: ArrayList<BarEntry> = ArrayList()
        for (i in dataList.indices) {
            values.add(BarEntry(i.toFloat(), dataList[i].totalGoal.toFloat()))
        }
        val set1: BarDataSet
        if (binding.chart.data != null &&
            binding.chart.data.dataSetCount > 0
        ) {
            set1 = binding.chart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            binding.chart.data.notifyDataChanged()
            binding.chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Data Set")
            set1.setDrawValues(true)
            val dataSets: ArrayList<IBarDataSet> = ArrayList()
            dataSets.add(set1)
            set1.colors = listOf(R.color.purple_500, R.color.teal_700)
            val data = BarData(dataSets)
            binding.chart.data = data
            binding.chart.setVisibleXRange(1.toFloat(), 4.toFloat())
            binding.chart.setFitBars(true)
            val xAxis: XAxis = binding.chart.xAxis
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true

            val xData = mutableListOf<String>()
            dataList.forEach {
                xData.add(it.category)
            }
            xAxis.valueFormatter =
                IndexAxisValueFormatter(xData)
            for (set in binding.chart.data
                .dataSets) set.setDrawValues(!set.isDrawValuesEnabled)
            binding.chart.invalidate()
        }
    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {

        val dialog = MaterialDialog(requireContext(), dialogBehavior).show {
            title(R.string.title_chooser)
            customView(R.layout.custom_bottom_months, scrollable = true, horizontalPadding = true)
        }

        val adapter = MonthsRvAdapter().apply {
            onItemClickListener = {
                dialog.dismiss()
                viewModel.fetchSummaries(it.key)
            }
        }

        val list = viewModel.fetchMonthData()
        adapter.appendList(list)
        val customView = dialog.getCustomView()
        val rvMonths = customView.findViewById<RecyclerView>(R.id.rvMonths)
        rvMonths.adapter = adapter
        rvMonths.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

    }
}