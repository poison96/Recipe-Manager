//Name: Allan Osierda
//Student ID: 18064319
//User Name: 18064319
//Subject Code: CSE3OAD

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import javafx.collections.*;
import javafx.collections.transformation.*;
import javafx.scene.control.cell.*;
import javafx.beans.property.*;

public class RecipeMain extends Application
{
	private ObservableList<Recipe> tableData;
	private TableView<Recipe> tableView;
	private RecipeDSC recipeDSC;
	private RecipeStageMaker recipeStageMaker;

	public void start(Stage primaryStage)
	{
		build(primaryStage);
		primaryStage.setTitle("Recipe Manager");
		primaryStage.show();
	}

	public void build(Stage primaryStage)
	{
		try
		{
			loadData();
		}
		catch(Exception error)
		{
			// Error Alert
			Alert confirm = new Alert(Alert.AlertType.WARNING);
			confirm.setTitle("Error!");
			confirm.setHeaderText("An error has occured!");
			confirm.setContentText("There was an error loading the data from the database.");
		}


		Node tableViewArea = makeTableView();
		Node searchArea = makeSearchArea();
		Node addViewDeleteArea = makeViewAddDeleteArea(primaryStage);

		VBox root = new VBox();
		root.getChildren().addAll(searchArea, tableViewArea, addViewDeleteArea);


		Scene scene = new Scene(root, 1080, 500);

		primaryStage.setScene(scene);

		// visual aspects
		{
			root.setStyle("-fx-alignment: top-center; -fx-spacing: 10");
			scene.getStylesheets().add("recipe.css");
		}
	}


	// To load data from the database into tableData and then to tableView
	//
	private void loadData() throws Exception
	{
		recipeDSC = new RecipeDSC();

		recipeDSC.getPassword();
		recipeDSC.connect();

		List<Recipe> recipes = recipeDSC.findAll();

		tableData = FXCollections.observableArrayList();
		tableData.clear();
		tableData.addAll(recipes);

		tableView = new TableView<Recipe>();
		tableView.setItems(tableData);
	}


	// Create and return a VBox to be the root of the scene graph
	//
	private VBox makeSceneRoot()
	{
		return null;
	}


	// Create the area to allow the user to search the table view
	// The program should provide option to apply the search (a) to every
	// field, (b) to the ingredient field, or (c) the name field.
	//
	private Node makeSearchArea()
	{
		Label lblSearchText = new Label("Enter Search Text:");
		TextField txtSearchText = new TextField("");
		HBox searchHbox = new HBox(lblSearchText, txtSearchText);
		searchHbox.setStyle("-fx-spacing: 10; -fx-alignment:center");

		Label lblSearchBy = new Label("Search By: ");

		RadioButton rdBtnAny = new RadioButton("Any Field");
		RadioButton rdBtnIngredients = new RadioButton("Ingredients");
		RadioButton rdBtnName = new RadioButton("Recipe Name");

		HBox radioHbox = new HBox(lblSearchBy, rdBtnAny, rdBtnIngredients, rdBtnName);
		radioHbox.setStyle("-fx-spacing: 10; -fx-alignment:center");

		ToggleGroup rdBtnGroup = new ToggleGroup();

		rdBtnAny.setToggleGroup(rdBtnGroup);
		rdBtnAny.setSelected(true);
		rdBtnIngredients.setToggleGroup(rdBtnGroup);
		rdBtnName.setToggleGroup(rdBtnGroup);


		// 1. create a filtered list, which shows every row (due to p-> true)
		FilteredList<Recipe> filteredList =
			new FilteredList<>(tableData, p -> true);

		// 2. Create a sorted list with the filtered list
		SortedList<Recipe> sortedList = new SortedList<>(filteredList);

		// 3.	Bind the comparator property of sorted list to that of table view
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());

		//	4.	Make the sorted list the items of the table view
		tableView.setItems(sortedList);

		//Textfield listener
		txtSearchText.textProperty().addListener((observable, oldValue, newValue) ->
		{
			filteredList.setPredicate(recipe ->
			{
				//if filter is empty, display all recipes
				if(newValue == null || newValue.isEmpty())
				{
					return true;
				}

				//Check which radiobutton is selected

				RadioButton selected = (RadioButton) rdBtnGroup.getSelectedToggle();

				if(selected.getText().equals("Any Field"))
				{
					String filterString = newValue.toUpperCase();

					boolean b =  Integer.toString(recipe.getId()).toUpperCase().contains(filterString) || recipe.getName().toUpperCase().contains(filterString) ||
                            Integer.toString(recipe.getServes()).toUpperCase().contains(filterString) || recipe.getIngredients().toUpperCase().contains(filterString) ||
                            recipe.getRemarks().toUpperCase().contains(filterString);

                    return b;

				}
				else if(selected.getText().equals("Ingredients"))
				{
					String filterString = newValue.toUpperCase();
					boolean b = recipe.getIngredients().toUpperCase().contains(filterString);
					return b;
				}
				else if(selected.getText().equals("Recipe Name"))
				{
					String filterString = newValue.toUpperCase();
					boolean b = recipe.getName().toUpperCase().contains(filterString);
					return b;
				}
				else
				{
					return false;
				}
			});
		});


		//Radiobutton listener
		rdBtnGroup.selectedToggleProperty().addListener((event) ->
		{
			String newValue = txtSearchText.getText();
			filteredList.setPredicate(recipe ->
			{
				//if filter is empty, display all recipes
				if(newValue == null || newValue.isEmpty())
				{
					return true;
				}

				//Check which radiobutton is selected

				RadioButton selected = (RadioButton) rdBtnGroup.getSelectedToggle();

				if(selected.getText().equals("Any Field"))
				{
					String filterString = newValue.toUpperCase();

					boolean b =  Integer.toString(recipe.getId()).toUpperCase().contains(filterString) || recipe.getName().toUpperCase().contains(filterString) ||
                            Integer.toString(recipe.getServes()).toUpperCase().contains(filterString) || recipe.getIngredients().toUpperCase().contains(filterString) ||
                            recipe.getRemarks().toUpperCase().contains(filterString);

                    return b;

				}
				else if(selected.getText().equals("Ingredients"))
				{
					String filterString = newValue.toUpperCase();
					boolean b = recipe.getIngredients().toUpperCase().contains(filterString);
					return b;
				}
				else if(selected.getText().equals("Recipe Name"))
				{
					String filterString = newValue.toUpperCase();
					boolean b = recipe.getName().toUpperCase().contains(filterString);
					return b;
				}
				else
				{
					return false;
				}
			});
		});




		VBox searchAreaVbox = new VBox(searchHbox, radioHbox);
		searchAreaVbox.setStyle("-fx-spacing: 10; -fx-alignment:center");
		return searchAreaVbox;
	}


	//
	// Define the table view and put it in a HBox. Return the HBox
	//
	private Node makeTableView()
	{
		TableColumn<Recipe, String> idColumn =
			new TableColumn<Recipe, String>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("Id"));

		TableColumn<Recipe, String> nameColumn =
			new TableColumn<Recipe, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("Name"));
		nameColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));

		TableColumn<Recipe, String> servesColumn =
			new TableColumn<Recipe, String>("Serves");
		servesColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("Serves"));

		TableColumn<Recipe, String> ingredientsColumn =
			new TableColumn<Recipe, String>("Ingredients");
		ingredientsColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("Ingredients"));
		ingredientsColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
		// To enable text wrap around, specify the call method of the
		// Callback interface of the column's cellFactory property
		//

		ingredientsColumn.setCellFactory(
			// method call of Callback
			(TableColumn<Recipe, String> param) ->
			{
				return new TableCell<Recipe, String>()
				{
					 @Override
					 public void updateItem(String item, boolean empty)
					 {
						super.updateItem(item, empty);
						Text text = new Text(item);
						text.wrappingWidthProperty().bind(ingredientsColumn.widthProperty());
						this.setWrapText(true);
						setGraphic(text);
					 }
				};
			});


		TableColumn<Recipe, String> remarksColumn =
			new TableColumn<Recipe, String>("Remarks");
		remarksColumn.setCellValueFactory(new PropertyValueFactory<Recipe, String>("Remarks"));
		remarksColumn.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));

		//Add Columns to table
		tableView.getColumns().add(idColumn);
		tableView.getColumns().add(nameColumn);
		tableView.getColumns().add(servesColumn);
		tableView.getColumns().add(ingredientsColumn);
		tableView.getColumns().add(remarksColumn);

		tableView.setItems(tableData);
		tableView.getSelectionModel().selectFirst();

		HBox tableHBox = new HBox(tableView);
		tableHBox.setHgrow(tableView, Priority.ALWAYS);

		return tableHBox;
	}


	// Create the area with the buttons to view, add and delete recipes
	//
	private Node makeViewAddDeleteArea(Stage primaryStage)
	{


		Button btnViewRecipe = new Button("View/Update Selected Recipe");
		Button btnAddRecipe = new Button("Add Recipe");
		Button btnDeleteRecipe = new Button("Delete Selected Recipe");

		HBox btnHbox = new HBox(btnViewRecipe, btnAddRecipe, btnDeleteRecipe);
		btnHbox.setStyle("-fx-spacing: 10; -fx-alignment:center");

		btnViewRecipe.setOnAction((e) ->
		{
			recipeStageMaker = new RecipeStageMaker(recipeDSC, tableData, tableView, primaryStage);
			recipeStageMaker.showViewRecipeStage();
		});

		btnAddRecipe.setOnAction((e) ->
		{
			recipeStageMaker = new RecipeStageMaker(recipeDSC, tableData, tableView, primaryStage);
			recipeStageMaker.showAddRecipeStage();
		});

		btnDeleteRecipe.setOnAction((e) ->
		{
			recipeStageMaker = new RecipeStageMaker(recipeDSC, tableData, tableView, primaryStage);
			recipeStageMaker.showDeleteRecipeStage();
		});

		return btnHbox;
	}
}
