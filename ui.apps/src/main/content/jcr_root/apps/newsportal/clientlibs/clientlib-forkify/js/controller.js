const icons = "/content/dam/newsportal/forkify/icons.svg";
console.log(icons);
let recipeContainer = document.querySelector(".recipe");

const renderSpinner = function (parentEl) {
  const markup = `
        <div class="spinner">
            <svg>
                <use href="${icons}#icon-loader"></use>
            </svg>
        </div>
    `;
  parentEl.innerHTML = "";
  parentEl.insertAdjacentHTML("afterbegin", markup);
};

const clearRecipeContainer = function () {
  recipeContainer.innerHTML = "";
};

const showRecipe = async function () {
  try {
    const id = window.location.hash.slice(1); // Get the ID from the hash
    if (!id) return; // If no ID in the hash, do nothing

    renderSpinner(recipeContainer);

    const res = await fetch(`/bin/forkify/api?id=${id}`); // Pass ID as query parameter
    const data = await res.json();
    if (!res.ok) throw new Error(`${data.message} (${data.status})`);
    console.log(res, data);

    let { recipe } = data.data;
    recipe = {
      id: recipe.id,
      title: recipe.title,
      publisher: recipe.publisher,
      sourceUrl: recipe.source_url,
      image: recipe.image_url,
      servings: recipe.servings,
      cookingTime: recipe.cooking_time,
      ingredients: recipe.ingredients,
    };

    const ingredientsMarkup = recipe.ingredients
      .map((ing) => {
        return `
        <li class="recipe__ingredient">
          <svg class="recipe__icon">
            <use href="${icons}#icon-check"></use>
          </svg>
          <div class="recipe__quantity">${ing.quantity}</div>
          <div class="recipe__description">
            <span class="recipe__unit">${ing.unit}</span>
            ${ing.description}
          </div>
        </li>
        `;
      })
      .join("");

    const markup = `
      <figure class="recipe__fig">
        <img
          src="${recipe.image}"
          alt="${recipe.title}"
          class="recipe__img"
        />
        <h1 class="recipe__title">
          <span>${recipe.title}</span>
        </h1>
      </figure>

      <div class="recipe__details">
        <div class="recipe__info">
          <svg class="recipe__info-icon">
            <use href="${icons}#icon-clock"></use>
          </svg>
          <span class="recipe__info-data recipe__info-data--minutes">${recipe.cookingTime}</span>
          <span class="recipe__info-text">minutes</span>
        </div>
        <div class="recipe__info">
          <svg class="recipe__info-icon">
            <use href="${icons}#icon-users"></use>
          </svg>
          <span class="recipe__info-data recipe__info-data--people">${recipe.servings}</span>
          <span class="recipe__info-text">servings</span>

          <div class="recipe__info-buttons">
            <button
              class="btn--tiny btn--update-servings"
              data-update-to="3"
            >
              <svg>
                <use
                  href="${icons}#icon-minus-circle"
                ></use>
              </svg>
            </button>
            <button
              class="btn--tiny btn--update-servings"
              data-update-to="5"
            >
              <svg>
                <use
                  href="${icons}#icon-plus-circle"
                ></use>
              </svg>
            </button>
          </div>
        </div>

        <div class="recipe__user-generated hidden">
          <svg>
            <use href="${icons}#icon-user"></use>
          </svg>
        </div>
        <button class="btn--round btn--bookmark">
          <svg class="">
            <use
              href="${icons}#icon-bookmark"
            ></use>
          </svg>
        </button>
      </div>

      <div class="recipe__ingredients">
        <h2 class="heading--2">Recipe ingredients</h2>
        <ul class="recipe__ingredient-list">
          ${ingredientsMarkup}
        </ul>
      </div>

      <div class="recipe__directions">
        <h2 class="heading--2">How to cook it</h2>
        <p class="recipe__directions-text">
          ${recipe.message}
          <span class="recipe__publisher">${recipe.publisher}</span>. Please check out
          directions at their website.
        </p>
        <a class="btn--small recipe__btn" href="${recipe.sourceUrl}" target="_blank">
          <span>Directions</span>
          <svg class="search__icon">
            <use href=""></use>
          </svg>
        </a>
      </div>
    `;
    clearRecipeContainer();
    recipeContainer.insertAdjacentHTML("afterbegin", markup);
    console.log(recipe);
  } catch (error) {
    console.log(error);
  }
};

window.addEventListener("hashchange", showRecipe);
window.addEventListener("load", showRecipe); // Ensure the recipe is shown on page load
