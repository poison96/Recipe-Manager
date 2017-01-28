--	This is a MySQL script to
--		Create a database
--		Create a table
--		Add some data to the table

drop database if exists recipedb; 
create database recipedb; 
use recipedb;

create table recipe(
  id int auto_increment,
  name varchar(50) not null,
  serves int,
  ingredients varchar(200) not null,
  steps text,
  remarks varchar(100),
  primary key (id)
);

-- Populate the database with some data

insert into recipe		-- nr 1
values(null, 'Breakfast Quinoa', 3,
'ALMOND MILK 2 cups; QUINOA 1 cup; BANANA 1 sliced, RASPBEERRIES 1 cup',
"\nBring milk to a boil\nAdd quinoa and return to a boil\nSimmer for 15 minutes\nAdd 3/4 of banana and raspberries\nCook until all milk is absorbed\nAdd remaing banana and raspberries",
'1300 KJ per serve'
);

insert into recipe 		-- nr 2
values(null, 'Sweet Potato Ham Fritters', 4,
'EGG 4 eggs; SWEET POTATO 2 cups of mashed potato; SMOKE HAM 100g; OLIVE OIL 1 table spoon',
'\n1. Beat the eggs in a large bowl.\n2. Add sweet potato and ham.\n3. heat 2 spoons of oive oil in  frying pan.\n4. Spoon the batter and cook until brown on each side.',
'High in fibre, low in carbohydrate'
);

insert into recipe		-- nr 3
values(null, 'Yoghurt Parfait', 1,
'BROWN RICE 1/2 cup of cooked rice; FAT-FREE YOGHURT 100g; BLUEBERRIES 50g; ALMOND 30g chopped',
'\nMix brown rice and yoghurt\nAdd blueberries and almond\nSpoon into a parfait glass to serve',
'1628 KJ per serve'
);

insert into recipe 		-- nr 4
values(null, 'Balsamic Chicken with Creamy Mash', 4,
'CHICKEN BREAST 500g; BALSAMIC VINEGAR 1/4 cup; GOLD POTATO 700g; AVOCADO 300g',
'\nSlice chicken into thin tenderloins\nMarinate in vinegar for 20 minutes\nHeat chicken at 180 degrees for 10 minuts\nPeel potato and boil them until tender\nDrain potato, and mash until smooth\nAdd chopped avacado and mash\nSpoon mash into a bowl and top with chicken strips',
'Low in energy'
);


-- to verify
select * from recipe\G;

