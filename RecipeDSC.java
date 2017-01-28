//Name: Allan Osierda
//Student ID: 18064319
//User Name: 18064319
//Subject Code: CSE3OAD

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Driver;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeDSC
{
	private  Connection connection;
	private  Statement statement;
	private  PreparedStatement preparedStatement;
	private  String password;

	public  void getPassword()
	{
		//char [] pwd = System.console().readPassword("Password: ");
		//password = new String(pwd);
		password = ""; //Password for the database
	}

	public  void connect() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");

		String url = "jdbc:mysql://"; //Database URL here
		String user = ""; //Enter Username here

		//String url = "jdbc:mysql://localhost:3306/recipedb";
		//String user = "";
		connection = DriverManager.getConnection(url, user, password);
		statement = connection.createStatement();
	}

	public  void disconnect() throws SQLException
	{
		if (preparedStatement != null) preparedStatement.close();
		if (statement != null) statement.close();
		if (connection != null) connection.close();
	}

	/*
	 * This method should find a Recipe with the given id.
	 * @param id The id of the Recipe to be found.
	 * @return The Recipe with the given id if it exists. Otherwise return null.
	 * @throws SQLException
	 */
	public Recipe find(int id) throws SQLException
	{
		ResultSet rs = statement.executeQuery("select * from recipe where id =" + id);

		int myid = -1;
		String name = "";
		int serves = -1;
		String ingredients = "";
		String steps = "";
		String remarks = "";

		while( rs.next() )
		{
			myid = rs.getInt("id");
			name = rs.getString("name");
			serves = rs.getInt("serves");
			ingredients = rs.getString("ingredients");
			steps = rs.getString("steps");
			remarks = rs.getString("remarks");
		}

		if (myid == -1)
		{
			return null;
		}
		else
		{
			Recipe recipe = new Recipe(myid, name, serves, ingredients, steps, remarks);
			return recipe;
		}

	}


	/*
	 * This method should count the total number of Recipes in the database
	 * @return An int representing the number of Recipes
	 * @throws SQLException
	 */
	public int count() throws SQLException
	{
		ResultSet rs = statement.executeQuery("select * from recipe");
		int count = 0;

		while( rs.next() )
		{
			count++;
		}
		return count;
	}


	/**
	 * This method should obtain and return  the list of all Recipes
	 * from the database
	 * @return The list of all stored Recipes
	 * @throws SQLException
	 */
	public List<Recipe> findAll() throws SQLException
	{
		List<Recipe> recipes = new ArrayList<Recipe>();

		ResultSet rs = statement.executeQuery("select * from recipe");

		while( rs.next() )
		{
			int id = rs.getInt("id");
			String name = rs.getString("name");
			int serves = rs.getInt("serves");
			String ingredients = rs.getString("ingredients");
			String steps = rs.getString("steps");
			String remarks = rs.getString("remarks");

			recipes.add(new Recipe(id, name, serves, ingredients, steps, remarks));
		}

		return recipes;
	}


	/*
	 * This method should try to add a new Recipe with the details
	 * provided by the parameters
	 *	@return The id of the recipe (which is generated)
	 * @param All the details of the recipe to be added (except the id)
	 * @throws SQLException
	 */
	public int add(String name, int serves, String ingredients, String steps, String remarks) throws SQLException
	{
		String query = "insert into recipe(name, serves, ingredients, steps, remarks) " +
					"values ('" + name + "', " + serves + ", '" + ingredients + "', '" + steps +"', '" + remarks +"')";


		int affectedRows = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		int id = -1;

		if (affectedRows != 0)
		{
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if(generatedKeys.next())
			{
				id = (int)generatedKeys.getLong(1);
			}
		}

		return id;
	}

	/*
	 * This method should try to add the given Recipe to the database.
	 * @param recipe The recipe instance, which contains details of the new recipe
	 * @throws SQLException
	 */

	public void add(Recipe recipe) throws SQLException
	{
		// The id of recipe should be simply ignored in the creation of the
		// new recipe

		String name = recipe.getName();
		int serves = recipe.getServes();
		String ingredients = recipe.getIngredients();
		String steps = recipe.getSteps();
		String remarks = recipe.getRemarks();

		String query = "insert into recipe(name, serves, ingredients, steps, remarks) " +
					"values ('" + name + "', " + serves + ", '" + ingredients + "', '" + steps +"', '" + remarks +"')";

		statement.executeQuery(query);
	}

	/**
	 * This method should try to update an existing Recipe using the
	 * details provided by the given Recipe parameter. All the details, except
	 * the id, can be updated
	 * @param recipe The Recipe instance that contains details to be used for
	 * updating
	 * @throws SQLException
	 */
	public void update(Recipe recipe) throws SQLException
	{
		int myid = recipe.getId();
		String name = recipe.getName();
		int serves = recipe.getServes();
		String ingredients = recipe.getIngredients();
		String steps = recipe.getSteps();
		String remarks = recipe.getRemarks();

		String query = "UPDATE recipe " +
				"SET name = '" + name + "', " +
				"serves = " + serves + ", " +
				"ingredients = '" + ingredients + "', " +
				"steps = '" + steps + "', " +
				"remarks = '" + remarks + "' " +
				"WHERE id = " + myid;
		statement.executeUpdate(query);

	}


	/**
	 * This method should try to delete a Recipe record from the database
	 * @param id The id of the Recipe to be deleted
	 * @throws SQLException
	 */

	public void delete(int id) throws SQLException
	{
		String query = "DELETE FROM recipe WHERE id = " + id;
		statement.executeUpdate(query);
	}


	/**
	 * This method should try to delete a Recipe record from the database
	 * @param Recipe The Recipe to be deleted
	 * @throws SQLException If the ID of the Recipe doesn't already exist
	 */
	public void delete(Recipe recipe) throws SQLException
	{
		int id = recipe.getId();
		String query = "DELETE FROM recipe WHERE id = " + id;
		statement.executeUpdate(query);
	}

	public static void main(String [] args) throws Exception
	{
		/*
		RecipeDSC dsc = new RecipeDSC();
		dsc.getPassword();

		dsc.connect();

		List<Recipe> list = dsc.findAll();
		System.out.println(list);

		Recipe recipe = dsc.find(4);
		System.out.println(recipe);

		recipe = dsc.find(100);
		System.out.println(recipe);

		int id = dsc.add("name 200", 100, "ingredients 200", "step 1 , 2, 3, 4", "easy");
		System.out.println("id: " + id);

		recipe = dsc.find(4);
		recipe.setName("Drunken chicken");
		recipe.setServes(10000);
		recipe.setIngredients("Drunken chicken 10 of them; RICE 100kg");
		recipe.setSteps("\n1. Cook chicken\n2.Cook rice");
		recipe.setRemarks("Enjoy the festival!");

		System.out.println(">>> updated recipe: " + recipe);

		dsc.update(recipe);

		dsc.disconnect();
		*/
	}

}
