package com.deepak.calendly.presenter.features.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.deepak.calendly.data.remote.model.Task
import com.deepak.calendly.presenter.navigation.NavigationItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(navController: NavController) {
    val viewModel: TaskViewmodel = hiltViewModel()
    val calendar = Calendar.getInstance()
    val currentMonth = remember { mutableStateOf(calendar.time) }

    val events = remember {
        mutableStateOf(getDatesInMonth(currentMonth.value))
    }

    val todayTasksLoaded = remember { mutableStateOf(false) }

    val onDateClick: (Date) -> Unit = { clickedDate ->
        val formattedDate = formatDate(clickedDate)
        viewModel.getCalenderTaskLists(formattedDate, 8209)
    }

    val onNextClick: () -> Unit = {
        calendar.time = currentMonth.value
        calendar.add(Calendar.MONTH, 1)
        currentMonth.value = calendar.time
        events.value = getDatesInMonth(currentMonth.value)
    }

    val onPrevClick: () -> Unit = {
        calendar.time = currentMonth.value
        calendar.add(Calendar.MONTH, -1)
        currentMonth.value = calendar.time
        events.value = getDatesInMonth(currentMonth.value)
    }

    LaunchedEffect(Unit) {
        if (!todayTasksLoaded.value) {
            val today = formatDate(Date())
            viewModel.getCalenderTaskLists(today, 8209)
            todayTasksLoaded.value = true
        }
    }

    CalendarView(
        month = currentMonth.value,
        date = events.value,
        displayNext = true,
        displayPrev = true,
        onClickNext = onNextClick,
        onClickPrev = onPrevClick,
        onClick = onDateClick,
        startFromSunday = true,
        viewmodel = viewModel,
        navController = navController,
        modifier = Modifier.fillMaxSize()
    )
}

private fun Date.formatToCalendarDay(): String =
    SimpleDateFormat("d", Locale.getDefault()).format(this)

@Composable
private fun CalendarCell(
    date: Date,
    signal: Boolean,
    isToday: Boolean,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = date.formatToCalendarDay()
    val borderColor = if (selected) Color(0xFF007AFF) else Color.Transparent
    val backgroundColor = if (isToday && !selected) Color(0xFF007AFF) else Color.White
    val textColor = if (isToday && !selected) Color.White else Color.Black

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(2.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(8.dp)),
                color = backgroundColor,
            )
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(CornerSize(8.dp))
            )
            .clip(RoundedCornerShape(CornerSize(8.dp)))
            .clickable(onClick = onClick)
    ) {
        if (signal) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    )
            )
        }
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


private fun Int.getDayOfWeek3Letters(): String? = Calendar.getInstance().apply {
    set(Calendar.DAY_OF_WEEK, this@getDayOfWeek3Letters)
}.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())

@Composable
private fun WeekdayCell(weekday: Int, modifier: Modifier = Modifier) {
    val text = weekday.getDayOfWeek3Letters()
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Text(
            text = text.orEmpty(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CalendarGrid(
    date: List<Pair<Date, Boolean>>,
    onClick: (Date) -> Unit,
    startFromSunday: Boolean,
    viewmodel: TaskViewmodel,
    modifier: Modifier = Modifier,
) {
    val today = Calendar.getInstance().time
    val weekdayFirstDay = date.first().first.day
    val weekdays = getWeekDays(startFromSunday)
    CalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            WeekdayCell(weekday = it)
        }
        repeat(if (!startFromSunday) weekdayFirstDay - 2 else weekdayFirstDay - 1) {
            Spacer(modifier = Modifier)
        }
        date.forEach {
            val isToday = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(it.first) ==
                    SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(today)
            CalendarCell(
                date = it.first,
                isToday = isToday,
                signal = it.second,
                selected = it.first == viewmodel.selectedDate,
                onClick = {
                    viewmodel.updateSelectedDate(it.first)
                    onClick(it.first)
                }
            )
        }
    }
}


fun getWeekDays(startFromSunday: Boolean): List<Int> {
    val list = (1..7).toList()
    return (if (startFromSunday) list else list.drop(1) + list.take(1)).toList()
}

@Composable
private fun CalendarCustomLayout(
    modifier: Modifier = Modifier,
    horizontalGapDp: Dp = 2.dp,
    verticalGapDp: Dp = 2.dp,
    content: @Composable () -> Unit,
) {
    val horizontalGap = with(LocalDensity.current) {
        horizontalGapDp.roundToPx()
    }
    val verticalGap = with(LocalDensity.current) {
        verticalGapDp.roundToPx()
    }
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val totalWidthWithoutGap = constraints.maxWidth - (horizontalGap * 6)
        val singleWidth = totalWidthWithoutGap / 7

        val xPos: MutableList<Int> = mutableListOf()
        val yPos: MutableList<Int> = mutableListOf()
        var currentX = 0
        var currentY = 0
        measurables.forEach { _ ->
            xPos.add(currentX)
            yPos.add(currentY)
            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += singleWidth + verticalGap
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxHeight = singleWidth, maxWidth = singleWidth))
        }

        layout(
            width = constraints.maxWidth,
            height = currentY + singleWidth + verticalGap,
        ) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = xPos[index],
                    y = yPos[index],
                )
            }
        }
    }
}

@Composable
fun CalendarView(
    month: Date,
    date: List<Pair<Date, Boolean>>?,
    displayNext: Boolean,
    displayPrev: Boolean,
    onClickNext: () -> Unit,
    onClickPrev: () -> Unit,
    onClick: (Date) -> Unit,
    startFromSunday: Boolean,
    viewmodel: TaskViewmodel,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    viewmodel.selectedDate?.let { selectedDate ->
        val formattedDate = formatDate(selectedDate)
        viewmodel.getCalenderTaskLists(formattedDate, 8209)
    }

    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (displayPrev)
                IconButton(
                    onClick = onClickPrev,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "navigate to previous month"
                    )
                }
            if (displayNext)
                IconButton(
                    onClick = onClickNext,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "navigate to next month"
                    )
                }
            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(month),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        CalendarGrid(
            date = date ?: listOf(),
            onClick = { clickedDate ->
                viewmodel.updateSelectedDate(clickedDate)
                onClick(clickedDate)
            },
            startFromSunday = startFromSunday,
            viewmodel = viewmodel,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        TaskHeader(viewmodel = viewmodel, onAddTaskClicked = { selectedDate ->
            val formattedDate = selectedDate?.let { formatDate(it) } ?: formatDate(Date())
            navController.navigate(NavigationItem.AddTask.createRoute(formattedDate))
        })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val taskList = viewmodel.tasks.value
            items(taskList?.size ?: 0) { index ->
                taskList?.get(index)?.let { task ->
                    TaskCard(task = task) {
                        viewmodel.deleteTask(userId = 8209, taskId = it)
                        viewmodel.getCalenderTaskLists(formatDate(viewmodel.selectedDate?: Date()), 8209)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onDeleteClicked: (taskId: Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = task.taskDetail.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.taskDetail.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = {
                onDeleteClicked(task.taskId)
            },
            modifier = Modifier
                .size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


fun getDatesInMonth(month: Date): List<Pair<Date, Boolean>> {
    val calendar = Calendar.getInstance()
    calendar.time = month
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val dates = mutableListOf<Pair<Date, Boolean>>()
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    for (i in 1..maxDay) {
        dates.add(Pair(calendar.time, false))
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return dates
}

@Composable
fun TaskHeader(
    onAddTaskClicked: (selectedDate: Date?) -> Unit,
    viewmodel: TaskViewmodel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFF007AFF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Daily Tasks",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                onAddTaskClicked(viewmodel.selectedDate)
            },
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
                .size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Task",
                tint = Color(0xFF007AFF),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    return formatter.format(date)
}