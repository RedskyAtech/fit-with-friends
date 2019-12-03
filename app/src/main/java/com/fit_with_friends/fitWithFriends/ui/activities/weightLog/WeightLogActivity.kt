package com.fit_with_friends.fitWithFriends.ui.activities.weightLog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.format.DateFormat
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IWeightLogService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.common.helpers.EventDecorator
import com.fit_with_friends.common.helpers.MySelectorDecorator
import com.fit_with_friends.common.helpers.OneDayDecorator
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.prolificinteractive.materialcalendarview.*
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_weight_log.*
import java.text.*
import java.util.*
import javax.inject.Inject

class WeightLogActivity : BaseAppCompactActivity(), View.OnClickListener, OnDateSelectedListener,
    OnMonthChangedListener {

    @Inject
    lateinit var iWeightLogService: IWeightLogService

    lateinit var widget: MaterialCalendarView

    private var mDate = CalendarDay()

    private lateinit var lineChart: LineChart
    private lateinit var barChart: BarChart

    private lateinit var oneDayDecorator: OneDayDecorator
    private lateinit var mySelectorDecorator: MySelectorDecorator

    private var list: MutableList<Float> = ArrayList()
    private var weightLogModelList: MutableList<WeightLogModel> = ArrayList()

    var mWeightLogModel = WeightLogModel()

    private var isYearSelected: Boolean = false
    private var weightLogType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_log)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        initView()
        listeners()
        setCalenderData()
    }

    private fun initView() {
        widget = findViewById(R.id.widget)
        oneDayDecorator = OneDayDecorator(this)
        mySelectorDecorator = MySelectorDecorator(this)
        lineChart = findViewById(R.id.lineChart)
        barChart = findViewById(R.id.barChart)
        val rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_animation) as RotateAnimation
        textView_weight_new.animation = rotate
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {
        widget.setOnMonthChangedListener(this)
        widget.setOnDateChangedListener(this)

        rLayout_previous_month.setOnClickListener {
            widget.goToPrevious()
        }

        rLayout_previous.setOnClickListener {
            widget.goToPrevious()
        }

        rLayout_next_month.setOnClickListener {
            widget.goToNext()
        }

        rLayout_next.setOnClickListener {
            widget.goToNext()
        }

        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        textView_month.setOnClickListener {
            widget.isPagingEnabled = true
            isYearSelected = false
            textView_month.text = resources.getString(R.string.text_month_underline)
            textView_year.text = resources.getString(R.string.text_year)
            textView_month.setTextColor(ContextCompat.getColor(applicationContext, R.color.color_purple))
            textView_year.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            weightLogType = 1
            getWeightLogsFromServer(mDate, 1)
            textView_day.text = "Days"
            val monthOfYear = DateFormat.format("MMMM", mDate.date) as String
            if (textView_current_month != null) {
                textView_current_month.text = monthOfYear
            }
            if (textView_current != null) {
                textView_current.text = monthOfYear
            }
            if (rLayout_previous_month != null) {
                rLayout_previous_month.visibility = View.VISIBLE
            }
            if (rLayout_next_month != null) {
                rLayout_next_month.visibility = View.VISIBLE
            }
            if (rLayout_previous != null) {
                rLayout_previous.visibility = View.VISIBLE
            }
            if (rLayout_next != null) {
                rLayout_next.visibility = View.VISIBLE
            }
        }

        textView_year.setOnClickListener {
            widget.isPagingEnabled = false
            isYearSelected = true
            textView_year.text = resources.getString(R.string.text_year_underline)
            textView_month.text = resources.getString(R.string.text_month)
            textView_month.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            textView_year.setTextColor(ContextCompat.getColor(applicationContext, R.color.color_purple))
            weightLogType = 2
            getWeightLogsFromServer(mDate, 2)
            textView_day.text = "Months"
            val monthOfYear = DateFormat.format("MMMM", mDate.date) as String
            val year = DateFormat.format("yyyy", mDate.date) as String
            if (textView_current_month != null) {
                textView_current_month.text = year
            }
            if (textView_current != null) {
                textView_current.text = monthOfYear
            }
            if (rLayout_previous_month != null) {
                rLayout_previous_month.visibility = View.GONE
            }
            if (rLayout_next_month != null) {
                rLayout_next_month.visibility = View.GONE
            }
            if (rLayout_previous != null) {
                rLayout_previous.visibility = View.GONE
            }
            if (rLayout_next != null) {
                rLayout_next.visibility = View.GONE
            }
        }
    }

    private fun setCalenderData() {
        widget.showOtherDates = MaterialCalendarView.SHOW_DEFAULTS

        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large)
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Small)
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Small)
        widget.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit()
        widget.topbarVisible = false

        val instance = Calendar.getInstance()
        widget.setSelectedDate(instance.time)

        val instance1 = Calendar.getInstance()
        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1)

        val instance2 = Calendar.getInstance()
        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31)

        widget.state().edit()
            .setMinimumDate(instance1.time)
            .setMaximumDate(instance2.time)
            .setFirstDayOfWeek(Calendar.MONDAY)
            .commit()

        widget.addDecorators(
            mySelectorDecorator,
            oneDayDecorator
            //TODO previous dates disable
            //HighlightWeekendsDecorator()
        )

        widget.selectionColor = ContextCompat.getColor(applicationContext, R.color.color_light_sky_blue)
    }

    private fun getWeightLogsFromServer(date: CalendarDay?, log_type: Int) {
        startAnim()
        val input = PageInput()
        input.query.add("log_type", log_type)
        if (date != null) {
            input.query.add("month", date.month + 1)
            input.query.add("year", date.year)
        }
        iWeightLogService.getWeightLogsFromServer(
            Constants.BASE_URL + Constants.WEIGHT_LOG,
            null,
            input,
            object : AsyncResult<WeightLogModel> {
                override fun success(weightLogModel: WeightLogModel) {
                    runOnUiThread {
                        stopAnim()
                        if (weightLogModel.weight_log != null) {
                            mWeightLogModel = weightLogModel
                            weightLogModelList.clear()
                            weightLogModelList.addAll(weightLogModel.weight_log)
                            list.clear()
                            for (i in 0 until weightLogModel.weight_log.size) {
                                try {
                                    val weightValue: Float = weightLogModel.weight_log[i].weight
                                    if (weightLogModel.weight_log[i].weight_type == 1) {
                                        val weightLbs = weightValue * 2.2046226218
                                        list.add(DecimalFormat("##.##").format(weightLbs).toFloat())
                                    } else {
                                        list.add(weightValue)
                                    }
                                } catch (ex: NumberFormatException) {
                                    ex.printStackTrace()
                                }
                            }
                            setDataInLineChart(list.size, list)
                            setDataInBarChart(list.size, list)

                            if (!isYearSelected) {
                                val mMonthlyWeightList: HashMap<String, List<CalendarDay>> = setUpCalenderDataMonthly()
                                setDataIntoCalendarMonthly(mMonthlyWeightList)
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun setDataInLineChart(count: Int, range: MutableList<Float>) {
        range.add(0, 0.0.toFloat())
        lineChart.clear()
        val values = ArrayList<Entry>()
        val values1 = ArrayList<Entry>()

        for (i in 0 until count) {
            val `val` = range[i]
            if (`val`.toDouble() != 0.0) {
                values.add(
                    Entry(
                        i.toFloat(),
                        `val`,
                        ContextCompat.getDrawable(applicationContext, R.drawable.default_dot)
                    )
                )
            } else {
                values1.add(
                    Entry(
                        i.toFloat(),
                        `val`,
                        ContextCompat.getDrawable(applicationContext, R.color.transparent)
                    )
                )
            }
        }

        val set1: LineDataSet
        val set2: LineDataSet

        if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
            set1 = lineChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            lineChart.data.notifyDataChanged()
            lineChart.notifyDataSetChanged()
        } else {
            // create a dataSet and give it a type
            set1 = LineDataSet(values, "")
            set2 = LineDataSet(values1, "")
            set2.setDrawIcons(false)
            set2.setDrawValues(false)
            set2.valueTextColor = Color.TRANSPARENT
            set2.color = Color.TRANSPARENT
            set2.setCircleColor(Color.TRANSPARENT)
            //set2.setDrawCircles(false)

            set1.setDrawIcons(false)
            // draw points as solid circles
            set1.setDrawCircleHole(false)

            set1.valueTextColor = Color.WHITE
            // text size of values
            set1.valueTextSize = 7f

            // draw dashed line
            //set1.enableDashedLine(10f, 5f, 0f)

            // white lines and points
            set1.color = Color.WHITE
            set1.setCircleColor(Color.WHITE)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { _, _ -> lineChart.axisLeft.axisMinimum }

            // set color of filled area
            set1.fillColor = ContextCompat.getColor(applicationContext, R.color.color_purple)
            //set1.fillColor = Color.TRANSPARENT

            //lineChart.setVisibleXRangeMaximum(31f)

            lineChart.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_purple))

            lineChart.setScaleEnabled(false)
            lineChart.setTouchEnabled(false)

            lineChart.xAxis.isEnabled = false
            lineChart.axisLeft.isEnabled = false
            lineChart.axisRight.isEnabled = false
            lineChart.xAxis.setDrawAxisLine(false)
            lineChart.xAxis.setDrawGridLines(false)
            lineChart.axisLeft.setDrawAxisLine(false)
            lineChart.axisLeft.setDrawGridLines(false)
            lineChart.axisRight.setDrawAxisLine(false)
            lineChart.axisRight.setDrawGridLines(false)

            //lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            val rightXAxis = lineChart.axisLeft
            rightXAxis.isEnabled = false

            val rightYAxis = lineChart.axisRight
            rightYAxis.isEnabled = false

            // hide legend
            val legend = lineChart.legend
            legend.isEnabled = false

            lineChart.description.isEnabled = false


            val xa = lineChart.xAxis
            xa.position = XAxis.XAxisPosition.BOTTOM

            val dataSets = ArrayList<ILineDataSet>()
            if (values.size != 0) {
                dataSets.add(set1) // add the data sets
            }
            if (values1.size != 0) {
                dataSets.add(set2) // add the data sets
            }

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            lineChart.data = data
        }
    }

    private fun setDataInBarChart(count: Int, range: MutableList<Float>) {
        barChart.clear()
        val values = ArrayList<BarEntry>()
        val values1 = ArrayList<BarEntry>()

        for (i in 0 until count) {
            val `val` = range[i]
            if (`val`.toDouble() != 0.0) {
                values.add(
                    BarEntry(
                        i.toFloat(),
                        `val`,
                        ContextCompat.getDrawable(applicationContext, R.drawable.default_dot)
                    )
                )
            } else {
                values1.add(
                    BarEntry(
                        i.toFloat(),
                        `val`,
                        ContextCompat.getDrawable(applicationContext, R.color.transparent)
                    )
                )
            }
        }

        val set1: BarDataSet
        val set2: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            set1 = barChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()

        } else {
            set1 = BarDataSet(values, "")
            set2 = BarDataSet(values1, "")
            set2.setDrawIcons(false)
            set2.setDrawValues(false)
            set2.valueTextColor = Color.TRANSPARENT
            set2.color = Color.TRANSPARENT

            set1.setDrawIcons(false)


            set1.valueTextColor = Color.WHITE
            // text size of values
            set1.valueTextSize = 5f

            // draw dashed line
            //set1.enableDashedLine(10f, 5f, 0f)

            // white lines and points
            set1.color = Color.WHITE

            barChart.setDrawBarShadow(false)
            barChart.setDrawValueAboveBar(true)

            // scaling can now only be done on x- and y-axis separately
            barChart.setPinchZoom(false)

            barChart.setDrawGridBackground(false)

            barChart.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.color_purple))

            barChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.white) // left y-axis
            barChart.xAxis.textColor = ContextCompat.getColor(this, R.color.white) // bottom xAxis

            barChart.setScaleEnabled(false)
            barChart.setTouchEnabled(false)

            barChart.xAxis.isEnabled = true
            barChart.xAxis.setDrawAxisLine(true)
            barChart.xAxis.setDrawGridLines(false)
            barChart.axisLeft.isEnabled = true
            barChart.axisLeft.setDrawAxisLine(true)
            barChart.axisLeft.setDrawGridLines(false)
            barChart.axisRight.isEnabled = false
            barChart.axisRight.setDrawAxisLine(false)
            barChart.axisRight.setDrawGridLines(false)

            val xAxis = barChart.xAxis
            xAxis.isEnabled = true
            val xa = barChart.xAxis
            xa.position = XAxis.XAxisPosition.BOTTOM

            val leftXAxis = barChart.axisLeft
            leftXAxis.isEnabled = true

            val rightYAxis = barChart.axisRight
            rightYAxis.isEnabled = false

            /*// legend
            val legend = barChart.legend
            legend.isEnabled = true*/

            barChart.axisLeft.spaceBottom = 0f

            val l = barChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.form = Legend.LegendForm.SQUARE
            l.formSize = 9f
            l.textSize = 11f
            l.xEntrySpace = 4f

            barChart.description.isEnabled = false

            val dataSets = ArrayList<IBarDataSet>()
            if (values.size != 0) {
                dataSets.add(set1) // add the data sets
            }
            if (values1.size != 0) {
                dataSets.add(set2) // add the data sets
            }

            val data = BarData(dataSets)
            data.setValueTextSize(5f)
            data.barWidth = 0.5f

            barChart.data = data
        }

    }

    private fun setUpCalenderDataMonthly(): HashMap<String, List<CalendarDay>> {
        val monthlyWeightLogList = ArrayList<CalendarDay>()
        val mHashList = HashMap<String, List<CalendarDay>>()
        for (weightModel: WeightLogModel in weightLogModelList) {
            if (weightModel.date != null && weightModel.date != "") {
                @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = sdf.parse(weightModel.date)
                val calendar = Calendar.getInstance()
                val dayOfMonth: Int = DateHelper.stringify(date, "dd").toInt()
                val month: Int = DateHelper.stringify(date, "MM").toInt()
                val year: Int = DateHelper.stringify(date, "yyyy").toInt()
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.YEAR, year)
                monthlyWeightLogList.add(CalendarDay.from(calendar.time))
                mHashList["weight"] = monthlyWeightLogList
                widget.addDecorator(
                    EventDecorator(
                        ContextCompat.getColor(applicationContext, R.color.color_green_success),
                        monthlyWeightLogList
                    )
                )
            }
        }
        return mHashList
    }

    private fun setDataIntoCalendarMonthly(mSlotsSortedList: HashMap<String, List<CalendarDay>>) {
        val keys = mSlotsSortedList.keys
        var calendarDays: List<CalendarDay>?
        val iterator = keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            calendarDays = mSlotsSortedList[key]
            if (key == "weight") {
                widget.addDecorator(
                    EventDecorator(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.color_green_success
                        ), calendarDays
                    )
                )
            }
        }
    }

    override fun onClick(view: View?) {

    }

    @SuppressLint("SetTextI18n")
    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        oneDayDecorator.setDate(date.date)
        widget.invalidateDecorators()
        val dayOfMonth: String = if (date.day < 10) {
            "0" + date.day.toString()
        } else {
            date.day.toString()
        }
        val monthNew: Int = date.month + 1
        val month: String
        month = if (monthNew < 10) {
            "0$monthNew"
        } else {
            monthNew.toString()
        }
        val year: String = date.year.toString()
        val selectedDate = "$year-$month-$dayOfMonth"
        getWeightDetail(selectedDate)
    }

    private fun getWeightDetail(selectedDate: String) {
        startAnim()
        val input = PageInput()
        input.query.add("date", selectedDate)
        iWeightLogService.getWeightLogsFromServer(
            Constants.BASE_URL + Constants.WEIGHT_LOG_DETAIL,
            null,
            input,
            object : AsyncResult<WeightLogModel> {
                @SuppressLint("SetTextI18n")
                override fun success(weightLogModel: WeightLogModel) {
                    runOnUiThread {
                        stopAnim()
                        if (weightLogModel.weight_kg != 0.0) {
                            layout_user_detail.visibility = View.VISIBLE
                            val weight: Float = weightLogModel.weight
                            val weightValue: String = weight.toString()
                            if (weightLogModel.weight_type == 1) {
                                textView_weight.text = "$weightValue kg"
                                setData(mWeightLogModel, weightLogModel)
                            } else {
                                textView_weight.text = "$weightValue lbs"
                                setData(mWeightLogModel, weightLogModel)
                            }
                            scrollToDown()
                        } else {
                            layout_user_detail.visibility = View.GONE
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        layout_user_detail.visibility = View.GONE
                    }
                }
            })
    }

    private fun setData(mWeightLogModel: WeightLogModel, weightLogModel: WeightLogModel) {
        if (mWeightLogModel.user.image != null && mWeightLogModel.user.image != "") {
            if (mWeightLogModel.user.image.contains("public")) {
                Glide.with(this).load(Constants.BASE_URL_IMAGE + mWeightLogModel.user.image)
                    .into(imageView_user_new)
            } else {
                Glide.with(this)
                    .load(Constants.BASE_URL_IMAGE + "public/" + mWeightLogModel.user.image)
                    .into(imageView_user_new)
            }
        } else {
            imageView_user_new.setImageResource(R.mipmap.harry)
        }

        if (mWeightLogModel.user.name != null) {
            textView_user_name.text = mWeightLogModel.user.name
        }

        if (weightLogModel.created_at != null) {
            textView_date.text = convertStringToData(weightLogModel.created_at)
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "dd-MM-yyyy")
    }

    private fun scrollToDown() {
        if (scrollView != null) {
            scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
        mDate = date!!
        val monthOfYear = DateFormat.format("MMMM", date.date) as String
        //val year = DateFormat.format("yyyy", date.date) as String
        //textView_current_month.text = "$monthOfYear $year"
        if (textView_current_month != null) {
            textView_current_month.text = monthOfYear
        }
        if (textView_current != null) {
            textView_current.text = monthOfYear
        }
        getWeightLogsFromServer(date, weightLogType)
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (avi_loader_weight_log != null) {
            avi_loader_weight_log.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_weight_log != null) {
            avi_loader_weight_log.smoothToHide()
        }
    }
}