import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.deepak.calendly.data.remote.model.TaskRequest
import com.deepak.calendly.presenter.features.addtask.AddTaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    date: String?
) {
    val viewModel: AddTaskViewModel = hiltViewModel()

    val taskSaved by viewModel.taskSaved

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(taskSaved) {
        if (taskSaved == true) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = Color.Red,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Add Task",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(onClick = {
                if (title.isNotBlank() && description.isNotBlank()) {
                    val taskDate = date ?: SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
                    println("Task Date: $date")
                    val taskDetail = TaskRequest.TaskModel(
                        title = title,
                        description = description,
                        date = taskDate!!
                    )
                    viewModel.storeCalenderTask(8209, taskDetail)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { newValue ->
                if (newValue.split(" ").size <= 20) {
                    title = newValue
                }
            },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            isError = title.split(" ").size > 20,
            placeholder = { Text("Enter task title (Max 20 words)") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${title.split(" ").size}/20 words",
            style = MaterialTheme.typography.bodyMedium,
            color = if (title.split(" ").size > 20) Color.Red else Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { newValue ->
                if (newValue.split(" ").size <= 100) {
                    description = newValue
                }
            },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5,
            isError = description.split(" ").size > 100,
            placeholder = { Text("Enter task description (Max 100 words)") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${description.split(" ").size}/100 words",
            style = MaterialTheme.typography.bodyMedium,
            color = if (description.split(" ").size > 100) Color.Red else Color.Gray
        )
    }
}
