package com.rafael0112.asessment2.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rafael0112.asessment2.R
import com.rafael0112.asessment2.ui.theme.Asessment2Theme
import com.rafael0112.asessment2.util.ViewModelFactory

const val KEY_ID_CATATAN = "idMyDiary"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)
    
    var catatan by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var hari by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getDiary(id) ?: return@LaunchedEffect
        catatan = data.catatan
        mood = data.mood
        hari = data.hari
        visible = data.visible
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null) {
                        visible = true
                        Text(text = stringResource(id = R.string.tambah_catatan))
                    } else {
                        if (visible) {
                            Text(text = stringResource(id = R.string.edit_catatan))
                        } else {
                            Text(text = stringResource(id = R.string.diary))
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (visible) {
                        IconButton(onClick = {
                            if (catatan == "" || mood == "") {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) {
                                viewModel.insert(catatan, mood)
                            } else {
                                viewModel.update(id, catatan, mood)
                            }
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = stringResource(R.string.simpan),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (id != null) {
                            DeleteAction {
                                showDialog = true
                            }
                        }
                    } else {
                        if (id != null) {
                            RecycleAction(
                                {
                                    showDialog = true
                                }, {
                                    viewModel.restore(id)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormCatatan(
            hari = hari,
            mood = mood,
            onTitleChange = { mood = it },
            desc = catatan,
            onDescChange = { catatan = it },
            visible = visible,
            modifier = Modifier.padding(padding)
        )
        if (id != null && showDialog) {
            DisplayAlertDialogRecycle(
                message = if (visible) {
                    stringResource(R.string.pesan_recycle)
                } else {
                    stringResource(R.string.pesan_hapus)
                },
                onDismissRequest = { showDialog = false },
                onConfirmation = {
                    showDialog = false
                    if (visible) {
                        viewModel.delete(id)
                    } else {
                        viewModel.permDelete(id)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun FormCatatan(
    mood: String, onTitleChange: (String) -> Unit,
    desc: String, onDescChange: (String) -> Unit,
    hari: String,
    visible: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = hari
        )
        OutlinedTextField(
            value = mood,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.judul)) },
            singleLine = true,
            readOnly = !visible,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = desc,
            readOnly = !visible,
            onValueChange = { onDescChange(it) },
            label = { Text(text = stringResource(R.string.isi_catatan)) },
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.recycle))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun RecycleAction(permDelete: () -> Unit, restore: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false}
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.hapus_permanen))
                },
                onClick = {
                    expanded = false
                    permDelete()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.restore))
                },
                onClick = {
                    expanded = false
                    restore()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Asessment2Theme {
        DetailScreen(rememberNavController())
    }
}