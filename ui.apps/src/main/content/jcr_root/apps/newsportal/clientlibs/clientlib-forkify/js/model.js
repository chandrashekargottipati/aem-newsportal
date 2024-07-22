// For example, instead of:

// import myModule from './myModule';

// Use:

// const myModule = require('./myModule');

// And instead of:

// export default myFunction;

// Use:

// module.exports = myFunction;

const API_URL = require("./config.JS");
const helper = require("./helper.js");

const state = {
  recipe: {},
  search: {
    query: "",
    results: [],
  },
};

export const loadRecipe = async function (id) {
  try {
    const data = await helper.getJSON(`${API_URL}?id=${id}`);
    const { recipe } = data.data;
    state.recipe = {
      id: recipe.id,
      title: recipe.title,
      publisher: recipe.publisher,
      sourceUrl: recipe.source_url,
      image: recipe.image_url,
      servings: recipe.servings,
      cookingTime: recipe.cooking_time,
      ingredients: recipe.ingredients,
    };
    console.log(state.recipe);
  } catch (error) {
    throw error;
  }
};

// const searchRecipe = async function (query) {
//   try {
//     state.search.query = query;
//     const data = await getJSON(`${API_URL}?query=${query}`);
//     console.log(data);
//     // You might want to process and store the search results here

//     state.search.results = data.data.recipes.map((rec) => {
//       return {
//         id: rec.id,
//         title: rec.title,
//         publisher: rec.publisher,
//         image: rec.image_url,
//       };
//     });
//   } catch (error) {
//     console.error("Error searching recipes:", error);
//     throw error;
//   }
// };

// // Example usage (you can remove this if you're calling these functions elsewhere)
// searchRecipe("pizza");
