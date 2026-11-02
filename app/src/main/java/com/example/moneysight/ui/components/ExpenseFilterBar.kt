package com.moneysight.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.EntryTealContainer
import com.moneysight.app.ui.theme.ExpenseBorder
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.ExpenseSurface
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.MintGreenDark
import com.moneysight.app.ui.theme.MintGreenPrimary
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.viewmodel.ExpenseSortOrder

@Composable
fun ExpenseFilterBar(
    searchQuery: String,
    selectedCategory: String?,
    selectedGroupId: String?,
    groups: List<ExpenseGroup>,
    selectedSortOrder: ExpenseSortOrder,
    onSearchQueryChanged: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onGroupSelected: (String?) -> Unit,
    onSortOrderSelected: (ExpenseSortOrder) -> Unit,
    onClearFiltersClicked: () -> Unit,
    onEditBillClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSortDropdown by remember { mutableStateOf(false) }
    var isSearchExpanded by remember { mutableStateOf(searchQuery.isNotEmpty()) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search, Sort, Edit & Delete Controls Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Toggle Button
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSearchExpanded) ExpenseRose.copy(alpha = 0.2f) else ExpenseSurface)
                    .border(
                        if (isSearchExpanded) 1.dp else 0.dp,
                        if (isSearchExpanded) ExpenseRose else androidx.compose.ui.graphics.Color.Transparent,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { isSearchExpanded = !isSearchExpanded }
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Expenses",
                        tint = ExpenseRose,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Sort Dropdown Button
            Box {
                Box(
                    modifier = Modifier
                        .height(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ExpenseSurface)
                        .clickable { showSortDropdown = true }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort",
                            tint = ExpenseRose,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedSortOrder.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark,
                            fontSize = 11.sp
                        )
                    }
                }

                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false },
                    modifier = Modifier.background(ForestSurface)
                ) {
                    ExpenseSortOrder.entries.forEach { order ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = order.label,
                                    color = if (order == selectedSortOrder) ExpenseRose else TextPrimaryOnDark,
                                    fontWeight = if (order == selectedSortOrder) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onSortOrderSelected(order)
                                showSortDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Joined Edit & Delete Bills Action Button
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MintGreenDark)
                    .clickable { onEditBillClicked() }
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit or Delete Bills",
                        tint = MintGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit / Delete",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MintGreenPrimary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Expanded Full-Width Search Input Field
        AnimatedVisibility(visible = isSearchExpanded) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search expenses by description...", color = TextMutedOnDark.copy(alpha = 0.6f), fontSize = 12.sp) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ExpenseRose,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onSearchQueryChanged("")
                            isSearchExpanded = false
                            keyboardController?.hide()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Close search",
                            tint = TextSecondaryOnDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = ExpenseSurface,
                    unfocusedContainerColor = ExpenseSurface,
                    focusedBorderColor = ExpenseRose,
                    unfocusedBorderColor = ExpenseBorder,
                    focusedTextColor = TextPrimaryOnDark,
                    unfocusedTextColor = TextPrimaryOnDark,
                    cursorColor = ExpenseRose
                )
            )
        }

        // Horizontal Group Filter Chips Row ONLY (People / Custom Expense Groups)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isAllGroupsSelected = selectedGroupId.isNullOrBlank()
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isAllGroupsSelected) EntryTeal else EntryTealContainer)
                    .clickable { onGroupSelected(null) }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "All Groups",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (isAllGroupsSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isAllGroupsSelected) OnEntryTeal else EntryTeal,
                    fontSize = 12.sp
                )
            }

            groups.forEach { group ->
                val isSelected = selectedGroupId == group.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) EntryTeal else EntryTealContainer)
                        .clickable { onGroupSelected(group.id) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = group.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) OnEntryTeal else EntryTeal,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
