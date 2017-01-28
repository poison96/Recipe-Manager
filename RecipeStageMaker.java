//Name: Allan Osierda
//Student ID: 18064319
//User Name: 18064319
//Subject Code: CSE3OAD

/*
This class is used to define and show three stages: one to view and/od update
a recipe, one to add a ne recipe, and one to delete a recipe.
*/

import javafx.application.Application;
import javafx.stage.*;
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

public class RecipeStageMaker
{
	private RecipeDSC recipeDSC;
	private ObservableList<Recipe> tableData;
	private TableView<Recipe> tableView;
	private Stage primaryStage;

	// id
	private Label idLB = new Label("Id: ");
	private TextField idTF = new TextField();
	private HBox idHBox = new HBox(idLB, idTF);

	// name
	private Label nameLB = new Label("Recipe Name: ");;
	private TextField nameTF = new TextField();
	private HBox nameHBox = new HBox(nameLB, nameTF);

	// serves
	private Label servesLB = new Label("Serves: ");
	private TextField servesTF = new TextField();
	private HBox servesHBox = new HBox(servesLB, servesTF);

	// ingredients
	private Label ingredientsLB = new Label("Ingredients: ");

	private TextField ingredientsTF = new TextField();

	private HBox ingredientsHBox = new HBox(ingredientsLB, ingredientsTF);

	// steps
	private TextArea stepsTA = new TextArea();
	private HBox stepsHBox = new HBox(stepsTA);

	// remarks
	private Label remarksLB = new Label("Remarks: ");
	private TextField remarksTF = new TextField();
	private HBox remarksHBox = new HBox(remarksLB, remarksTF);

	// action buttons
	private Button addBT = new Button("ADD Recipe");
	private Button updateBT = new Button("UPDATE Recipe");
	private Button deleteBT = new Button("DELETE Recipe");
	private Button cancelBT = new Button("EXIT/CANCEL");
	private HBox actionHBox = new HBox();

	// root, scene, local stage
	private VBox root = new VBox();
	private Scene scene = new Scene(root);
	private Stage stage = new Stage();

	public RecipeStageMaker(RecipeDSC recipeDSC, ObservableList<Recipe> tableData, TableView<Recipe> tableView, Stage primaryStage )
	{
		/*
		 * Initilize the RecipeStageMaker
		 */

		 this.recipeDSC = recipeDSC;
		 this.tableData = tableData;
		 this.tableView = tableView;
		 this.primaryStage = primaryStage;
	}

	public void showViewRecipeStage()
	{
		/*
		 * To present a stage to view and/or update the recipe selected
		 * in the table view
		 */

		stage.initStyle(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setX(primaryStage.getX() + 900);
		stage.setY(primaryStage.getY() + 400);

		//Get Selected Recipe
		Recipe recipe = tableView.getSelectionModel().getSelectedItem();

		//Set Text Fields with appropriate data
		idTF.setText(Integer.toString(recipe.getId()));
		idTF.setEditable(false);

		nameTF.setText(recipe.getName());
		servesTF.setText(Integer.toString(recipe.getServes()));
		ingredientsTF.setText(recipe.getIngredients());
		stepsTA.setText(recipe.getSteps());
		remarksTF.setText(recipe.getRemarks());

		//Set Width of textfield
		ingredientsTF.setPrefWidth(400);
		remarksTF.setPrefWidth(400);

		cancelBT.setOnAction(e -> stage.close());

		updateBT.setOnAction(e ->
		{
			try
			{
				//Update Recipe in database and reload tableView
				recipe.setName(nameTF.getText());
				recipe.setServes(Integer.parseInt(servesTF.getText()));
				recipe.setIngredients(ingredientsTF.getText());
				recipe.setSteps(stepsTA.getText());
				recipe.setRemarks(remarksTF.getText());

				recipeDSC.update(recipe);

				//Update Tableview
				List<Recipe> recipes = recipeDSC.findAll();
				tableData = FXCollections.observableArrayList();
				tableData.clear();
				tableData.addAll(recipes);

				tableView.setItems(tableData);
				tableView.refresh();

				// ask for confirmation
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
				confirm.setTitle("Update Successful");
				confirm.setHeaderText("Recipe Updated");
				confirm.setContentText("The recipe has been successfully updated.");

				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK)
				{
					stage.close();
				}

			}
			catch (Exception error)
			{
				// Error Alert
				Alert confirm = new Alert(Alert.AlertType.WARNING);
				confirm.setTitle("Error!");
				confirm.setHeaderText("An error has occured!");
				confirm.setContentText("There was an error updating the recipe.");
				confirm.showAndWait();
			}



		});

		VBox viewingVbox = new VBox(idHBox, nameHBox, servesHBox, ingredientsHBox,
									stepsHBox, remarksHBox);
		viewingVbox.setStyle("-fx-padding: 10; -fx-spacing: 5");

		actionHBox.getChildren().addAll(updateBT, cancelBT);
		actionHBox.setStyle("-fx-alignment: center; -fx-border-color: BLACK; -fx-padding: 10; -fx-spacing: 5");

		root.getChildren().addAll(viewingVbox, actionHBox);
		root.setStyle("-fx-padding: 10");

		stage.setScene(scene);
		stage.setTitle("View/Update Recipe");
		stage.show();
	}

	public void showAddRecipeStage()
	{
		/*
		 * To present a stage to add a recipe
		 */
		stage.initStyle(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);

		cancelBT.setOnAction(e -> stage.close());

		addBT.setOnAction(e ->
		{
			//Add new recipe to database and tableview
			try
			{
				recipeDSC.add(nameTF.getText(),
										 Integer.parseInt(servesTF.getText()),
										 ingredientsTF.getText(),
										 stepsTA.getText(),
										 remarksTF.getText());

				//Update Tableview
				List<Recipe> recipes = recipeDSC.findAll();
				tableData = FXCollections.observableArrayList();
				tableData.clear();
				tableData.addAll(recipes);

				tableView.setItems(tableData);
				tableView.refresh();

				// ask for confirmation
				Alert confirm = new Alert(Alert.AlertType.INFORMATION);
				confirm.setTitle("Add Successful");
				confirm.setHeaderText("Recipe Added");
				confirm.setContentText("The recipe has been successfully added.");

				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK)
				{
					stage.close();
				}

			}
			catch(Exception error)
			{
				// Error Alert
				Alert confirm = new Alert(Alert.AlertType.WARNING);
				confirm.setTitle("Error!");
				confirm.setHeaderText("An error has occured!");
				confirm.setContentText("There was an error adding a new recipe.");
				confirm.showAndWait();
			}

		});

		VBox addingVbox = new VBox(nameHBox, servesHBox, ingredientsHBox,
									stepsHBox, remarksHBox);
		addingVbox.setStyle("-fx-padding: 10; -fx-spacing: 5");

		actionHBox.getChildren().addAll(addBT, cancelBT);
		actionHBox.setStyle("-fx-alignment: center; -fx-border-color: BLACK; -fx-padding: 10; -fx-spacing: 5");

		root.getChildren().addAll(addingVbox, actionHBox);
		root.setStyle("-fx-padding: 10");

		stage.setScene(scene);
		stage.setTitle("Add Recipe");
		stage.show();
	}

	public void showDeleteRecipeStage()
	{
		/*
		 * To present a stage to delete the recipe selected in
		 * the table view
		 */
		stage.initStyle(StageStyle.UTILITY);
		stage.initModality(Modality.APPLICATION_MODAL);

		//Get Selected Recipe
		Recipe recipe = tableView.getSelectionModel().getSelectedItem();

		//Set Text Fields with appropriate data
		idTF.setText(Integer.toString(recipe.getId()));
		idTF.setEditable(false);

		nameTF.setText(recipe.getName());
		nameTF.setEditable(false);

		servesTF.setText(Integer.toString(recipe.getServes()));
		servesTF.setEditable(false);

		ingredientsTF.setText(recipe.getIngredients());
		ingredientsTF.setEditable(false);

		remarksTF.setText(recipe.getRemarks());
		remarksTF.setEditable(false);

		cancelBT.setOnAction(e -> stage.close());

		deleteBT.setOnAction(e ->
		{
			//Delete recipe from database and tableview

			try
			{
				// ask for confirmation
				Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
				confirm.setTitle("Deleting Recipe");
				confirm.setHeaderText("You are about to delete a recipe");
				confirm.setContentText("Are you sure you want the recipe to be deleted?");

				Optional<ButtonType> result = confirm.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK)
				{
					recipeDSC.delete(Integer.parseInt(idTF.getText()));

					Alert success = new Alert(Alert.AlertType.CONFIRMATION);
					success.setTitle("Recipe Deleted");
					success.setHeaderText("Recipe Deleted");
					success.setContentText("Recipe has been successfully deleted.");
					if (result.isPresent() && result.get() == ButtonType.OK)
					{
						//Update Tableview
						List<Recipe> recipes = recipeDSC.findAll();
						tableData = FXCollections.observableArrayList();
						tableData.clear();
						tableData.addAll(recipes);

						tableView.setItems(tableData);
						tableView.refresh();

						stage.close();
					}

				}
				else //They cancel the delete
				{
					//Nothing happens
				}
			}
			catch( Exception error)
			{
				// Error Alert
				Alert confirm = new Alert(Alert.AlertType.WARNING);
				confirm.setTitle("Error!");
				confirm.setHeaderText("An error has occured!");
				confirm.setContentText("There was an error deleting the recipe.");
				confirm.showAndWait();
			}

		});

		VBox deletingVbox = new VBox(idHBox, nameHBox, servesHBox, ingredientsHBox, remarksHBox);
		deletingVbox.setStyle("-fx-padding: 10; -fx-spacing: 5");

		actionHBox.getChildren().addAll(deleteBT, cancelBT);
		actionHBox.setStyle("-fx-alignment: center; -fx-border-color: BLACK; -fx-padding: 10; -fx-spacing: 5");

		root.getChildren().addAll(deletingVbox, actionHBox);
		root.setStyle("-fx-padding: 10");

		stage.setScene(scene);
		stage.setTitle("Delete Recipe");
		stage.show();
	}
}
