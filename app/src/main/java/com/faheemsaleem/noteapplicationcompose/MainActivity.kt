package com.faheemsaleem.noteapplicationcompose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.faheemsaleem.noteapplicationcompose.ui.theme.NoteApplicationComposeTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteApplicationComposeTheme {
                val viewModel = viewModel<NoteViewModel>()
                val state = viewModel.state
                NoteMainMenuForReal(state = state, onAction = viewModel::onAction)

            }
        }
    }
}

@Composable
fun NoteMainMenu(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = getGreeting(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(top = 32.dp, start = 16.dp),
            maxLines = 3
        )

        Text(
            text = "The time is " + getTime(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.ExtraLight,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(top = 16.dp, start = 16.dp),
            maxLines = 3
        )

        //PopUpDialog()

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize(1f)
                .weight(1f)
                .padding(top = 32.dp)
        ) {
            NoteCard(title = "Example Note")
            NoteCard(title = "Title of the note", subtitle = "Subtitle of the note")
            NoteCard(
                title = "Tap add note to add a new note",
                subtitle = "This is the test of the subtitle"
            )
        }

        ExtendedFloatingActionButton(
            onClick = {  },
            icon = { Icon(Icons.Filled.Edit, null) },
            text = { Text(text = "Add Note") },
            modifier = modifier
                .align(Alignment.End)
                .padding(8.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteMainMenuForReal(
    state: Notes,
    modifier: Modifier = Modifier,
    onAction: (NoteActions) -> Unit
    ) {
    // Add this line to create a state for the dialog
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }

    var toggled by remember {
        mutableStateOf(false)
    }
    val animatedPadding by animateDpAsState(
        if (toggled) {
            0.dp
        } else {
            16.dp
        },
        label = "padding"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(animatedPadding)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                toggled = !toggled
            }
        ,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = getGreeting(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Light,
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(top = 32.dp, start = 16.dp),
            maxLines = 3
        )

        Text(
            text = "The time is " + getTime(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.ExtraLight,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier.padding(top = 16.dp, start = 16.dp),
            maxLines = 3
        )

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize(1f)
                .weight(1f)
                .padding(top = 32.dp)
        ) {
            items(state.list) {note ->
                val dismissState = rememberDismissState()  // Create a new dismissState for each NoteCard

                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
                    background = { /* You can provide a background if needed */ },
                    dismissContent = {
                        NoteCard(
                            title = note.title,
                            subtitle = note.subtitle,
                            onClick = {
                                // Set the title and subtitle states and show the dialog when the card is clicked
                                title = note.title
                                subtitle = note.subtitle
                                showDialog = true
                            }
                        ) {
                            // Call onAction with a DeleteNote action when the card is dismissed
                            onAction(NoteActions.DeleteNote(note))
                        }
                    }
                )
            }

//            NoteCard(title = "Example Note")
//            NoteCard(title = "Title of the note", subtitle = "Subtitle of the note")
//            NoteCard(title = "Tap add note to add a new note", subtitle = "This is the test of the subtitle")
        }

        ExtendedFloatingActionButton(
            onClick = { showDialog = true },
            icon = { Icon(Icons.Filled.Edit, null) },
            text = { Text(text = "Add Note") },
            modifier = modifier
                .align(Alignment.End)
                .padding(8.dp)
        )

        if (showDialog) {
            PopUpDialog(
                onDismissRequest = { showDialog = false },
                onAction = onAction,
                title = title,
                subtitle = subtitle
            )
        }
    }
}

@Preview(
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun GreetingPreview() {
    NoteApplicationComposeTheme {
        NoteMainMenu()
    }
}

@Composable
fun NoteCard(
    title: String,
    subtitle: String = "",
    onClick: () -> Unit = {},
    onDismissed: () -> Unit = {}
)
{
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, bottom = if (subtitle.isNotBlank()) 0.dp else 16.dp, end = 16.dp)
                ,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

}

@Composable
fun getGreeting(): String {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }
}

fun getTime(): String {
    val calendar = Calendar.getInstance()
    return "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
}

@Composable
fun PopUpDialog(
    onDismissRequest: () -> Unit,
    onAction: (NoteActions) -> Unit = {},
    title: String = "",
    subtitle: String = ""
) {
    // Create states for the title and subtitle
    var title by remember { mutableStateOf(title) }
    var subtitle by remember { mutableStateOf(subtitle) }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                ,
        ) {
            Text(text = "Add Note",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp))


            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = subtitle,
                onValueChange = { subtitle = it },
                label = { Text(text = "Subtitle") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Button(
                onClick = {
                    onAction(NoteActions.AddNote(Note(title, subtitle)))
                    // Clear the text fields
                    title = ""
                    subtitle = ""
                    // Dismiss the dialog
                    onDismissRequest()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                Text(text = "Save", textAlign = TextAlign.End)
            }
        }
        
    }
    
}
