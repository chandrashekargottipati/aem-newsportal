const icons = "/content/dam/newsportal/forkify/icons.svg";
console.log(icons);

const controlleRecipe = async function () {
  try {
    const id = window.location.hash.slice(1); // Get the ID from the hash

    if (!id) return; // If no ID in the hash, do nothing
    const recipeView = new RecipeView();
    // recipeView.renderSpinner(); // Note: Added parentheses here
    await loadRecipe(id);

    // let recipe = state.recipe;
    recipeView.render(state.recipe);
  } catch (error) {
    console.log("Error occurred:", error);
    // recipeView.renderError("we could not find");
  }
};

const controllSearchRecipe = async function () {
  try {
    await searchRecipe("pizza");
    console.log(state.search.results);
  } catch (error) {
    console.log(error);
  }
};

controllSearchRecipe();

[("hashchange", "load")].forEach((ev) =>
  window.addEventListener(ev, controlleRecipe)
);
// window.addEventListener("hashchange", controlleRecipe);
// window.addEventListener("load", showRecipe); // Ensure the recipe is shown on page load
