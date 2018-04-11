package zestybot

import java.time.ZonedDateTime

case class Meals(meals: Seq[Meal])

/**
  * {
  * "id": "5a78fc95d0f1b900132a516d",
  * "extref": "ITE424",
  * "delivery_date": "2018-02-26T11:45:00-08:00",
  * "delivery_location_address": "Iterable New Office 08/08",
  * "headcount": 95,
  * "meal_type": "lunch",
  * "only_visible_to_admins": false,
  * "restaurant_name": "Chicory",
  * "restaurant_description": "Chicory brings fresh, delicious, and well-sourced salads and toppings to your table. Our DIY salad bars include crisp veggies, nuts, seeds, croutons, and homemade dressings so you can build your salad just the way you like it.\r\n\r\n",
  * "restaurant_cuisine": "Sandwiches & Salads",
  * "restaurant_large_image": "",
  * "restaurant_full_image": "",
  * "is_sample": false,
  * "is_alternative": false,
  * "accepted_by_client": false,
  * "menu_pdf_url": "https://api.zesty.com/client_portal_api/meals/5a78fc95d0f1b900132a516d/menu.pdf"
  * },
q  */
case class Meal(id: String, delivery_date: ZonedDateTime, restaurant_name: String, restaurant_description: String)
case class MealDetail(meals: Seq[Meal], meal_items: Seq[MealItem])
case class MealItem(id: Long, quantity: Int, instructions: String, dish_id: Long)
/**
  * {
  * "dishes": [
  * {
  * "id": "34508",
  * "vegetarian": false,
  * "vegan": false,
  * "gluten_free": true,
  * "serving_size": "4 oz",
  * "name": "Roasted Petaluma Chicken with Sherry Marinated Bell Peppers",
  * "description": "Chicken breast, bell pepper, sherry vinegar, canola oil, olive oil, parsley, pepper, salt, white wine vinegar, sugar, coriander, juniper, bay leaf, cinnamon mustard seed, chili flake, clove",
  * "full_image_path": "https://res.cloudinary.com/hasty/image/upload/c_fill,h_560,w_837/Zesty-Source-2018-03-01_Chicken_w_Bell_Peppers_rfosk3",
  * "allergens": [],
  * "has_nutrition": true,
  * "has_image": false,
  * "image_id": "Zesty-Source-2018-03-01_Chicken_w_Bell_Peppers_rfosk3",
  * "calories": 193,
  * "carbohydrates": 2,
  * "protein": 25,
  * "fat": 9,
  * "incomplete_nutrition": false,
  * "dish_components": []
  * }
  * ]
  * }
  * @param dishes a one-element list
  */
case class Dishes(dishes: Seq[Dish])
case class Dish(id: String, vegetarian: Boolean, vegan: Boolean, gluten_free: Boolean, name: String, description: String, full_image_path: String)