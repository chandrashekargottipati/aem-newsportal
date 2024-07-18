// icons
const icons = "/content/dam/newsportal/forkify/icons.svg";

// State
const state = {
  recipe: {},
  search: {
    query: "",
    results: [],
    page: 1,
    resultsPerPage: 10,
  },
  bookmarks: [],
};

// View elements
const recipeDetailsDiv = document.querySelector(".recipe-details");
const parentElementSearch = document.querySelector(".topbar__search");
const parentResultElement = document.querySelector(".results");
const topbarBookmarkBtn = document.querySelector(".topbar__bookmark-btn");
const topbarBookmarkText = document.querySelector(".topbar__bookmark-text");
const topbarBookmarkTooltip = document.querySelector(
  ".topbar__bookmark-tooltip"
);

// Load bookmarks from localStorage
const loadBookmarks = function () {
  const storedBookmarks = localStorage.getItem("bookmarks");
  if (storedBookmarks) {
    state.bookmarks = JSON.parse(storedBookmarks);
  }
};

// Save bookmarks to localStorage
const saveBookmarks = function () {
  localStorage.setItem("bookmarks", JSON.stringify(state.bookmarks));
};

// Update bookmark display in header
const updateBookmarkDisplay = () => {
  if (state.bookmarks.length > 0) {
    const latestBookmark = state.bookmarks[state.bookmarks.length - 1];
    topbarBookmarkText.textContent = latestBookmark.title;
  } else {
    topbarBookmarkText.textContent = "Bookmarks";
  }
};

// Event listener for bookmark button click
topbarBookmarkBtn.addEventListener("click", function () {
  toggleBookmark();
});

// Controller: Event listeners when DOM is loaded
document.addEventListener("DOMContentLoaded", function () {
  loadBookmarks();
  parentResultElement.addEventListener("click", handleRecipeClick);
  addHandlerSearch(controllSearchResults);
  addHandlerBookmark(toggleBookmark);
  displayBookmarks(); // Ensure bookmarks are displayed when the page loads
});

// script.js
document.addEventListener("DOMContentLoaded", function () {
  const bookmarkBtn = document.querySelector(".topbar__bookmark-btn");
  const bookmarkTooltip = document.querySelector(".topbar__bookmark-tooltip");

  bookmarkBtn.addEventListener("click", function (event) {
    event.stopPropagation();
    bookmarkTooltip.style.display =
      bookmarkTooltip.style.display === "block" ? "none" : "block";
  });

  // Close the bookmark list if clicked outside of it
  document.addEventListener("click", function (event) {
    if (!bookmarkTooltip.contains(event.target)) {
      bookmarkTooltip.style.display = "none";
    }
  });
});

// Controller function to handle recipe click
async function handleRecipeClick(event) {
  event.preventDefault();
  const target = event.target.closest(".preview__link");

  if (!target) return;

  const recipeId = target.getAttribute("data-id");

  try {
    const res = await fetch(`/bin/recipes?id=${recipeId}`);
    const data = await res.json();
    const { recipe } = data;

    state.recipe = {
      id: recipe.id,
      title: recipe.title,
      publisher: recipe.publisher,
      sourceUrl: recipe.sourceUrl,
      image: recipe.image,
      servings: recipe.servings,
      cookingTime: recipe.cookingTime,
      ingredients: recipe.ingredients,
      bookmarked: isBookmarked(recipe.id),
    };

    displayRecipeDetails(state.recipe);
  } catch (error) {
    console.error("Error fetching recipe details:", error);
    recipeDetailsDiv.innerHTML =
      "<p>Error loading recipe details. Please try again later.</p>";
  }
}

// View function to display recipe details
function displayRecipeDetails(recipe) {
  const markup = generateMarkup(recipe);
  recipeDetailsDiv.innerHTML = markup;
}

// Controller function to handle search submission
const addHandlerSearch = function (handler) {
  parentElementSearch.addEventListener("submit", function (e) {
    e.preventDefault();
    handler();
  });
};

// Model function to load search results
const loadSearchResults = async function (query) {
  try {
    state.search.query = query;
    const res = await fetch(`/bin/recipes?query=${query}`);
    const data = await res.json();

    state.search.results = data.results.map((rec) => ({
      id: rec.id,
      title: rec.title,
      publisher: rec.publisher,
      image: rec.image,
    }));

    displaySearchResults(getSearchResultsPage(1));
  } catch (error) {
    console.error("Error loading search results:", error);
  }
};

// Model function to add a bookmark
const addBookmark = function (recipe) {
  if (!recipe || !recipe.id || !recipe.title || !recipe.sourceUrl) {
    // Ensure that only valid recipes are bookmarked
    return;
  }

  state.bookmarks.push(recipe);

  if (recipe.id == state.recipe.id) state.recipe.bookmarked = true;
  saveBookmarks(); // Save to localStorage
  displayBookmarks();
};

// Function to display bookmarks in the tooltip
function displayBookmarks() {
  const bookmarksList = document.getElementById("bookmarksList");
  bookmarksList.innerHTML = ""; // Clear existing bookmarks

  if (state.bookmarks.length === 0) {
    bookmarksList.innerHTML = "<p>No bookmarks yet.</p>";
  } else {
    state.bookmarks.forEach((bookmark) => {
      const bookmarkItem = document.createElement("div");
      bookmarkItem.className = "bookmark-item";
      bookmarkItem.innerHTML = `
        <img src="${bookmark.image}" alt="${bookmark.title}">
        <a href="${bookmark.sourceUrl}">${bookmark.title}</a>
      `;
      bookmarksList.appendChild(bookmarkItem);
    });
  }
}

// Model function to remove a bookmark
const removeBookmark = function (id) {
  const index = state.bookmarks.findIndex((el) => el.id === id);
  state.bookmarks.splice(index, 1);

  if (id == state.recipe.id) state.recipe.bookmarked = false;
  saveBookmarks(); // Save to localStorage
  displayBookmarks();
};

// Function to toggle bookmark
const toggleBookmark = function () {
  if (!state.recipe || !state.recipe.id) {
    // Ensure a recipe is loaded before toggling bookmark
    return;
  }

  if (state.recipe.bookmarked) {
    removeBookmark(state.recipe.id);
  } else {
    addBookmark(state.recipe);
  }

  displayRecipeDetails(state.recipe);
  updateBookmarkDisplay();
  updateHeaderBookmarkButton();
};

// Controller function to handle search results
const controllSearchResults = async function () {
  try {
    const query = getQuery();
    if (!query) return;
    await loadSearchResults(query);
    state.search.page = 1;
    displaySearchResults(getSearchResultsPage(1));
  } catch (error) {
    console.error("Error controlling search results:", error);
  }
};

// Model function to get search results by page
const getSearchResultsPage = function (page = state.search.page) {
  const start = (page - 1) * state.search.resultsPerPage;
  const end = page * state.search.resultsPerPage;
  return state.search.results.slice(start, end);
};

// View function to display search results
function displaySearchResults(recipes) {
  if (recipes.length === 0) {
    parentResultElement.innerHTML =
      '<p class="no-recipes-message">No recipes found. Please try a different search query.</p>';
  } else {
    const markup = generateSearchMarkup(recipes);
    parentResultElement.innerHTML = markup;
    displayPagination(state.search.page);
  }
}

// View function to get search query
const getQuery = function () {
  const query = parentElementSearch
    .querySelector(".topbar__search-input")
    .value.trim();
  clear();
  return query;
};

// View function to clear search input
const clear = function () {
  parentElementSearch.querySelector(".topbar__search-input").value = "";
};

// Model function to check if recipe is bookmarked
const isBookmarked = function (id) {
  return state.bookmarks.some((bookmark) => bookmark.id === id);
};

// View function to update UI of header bookmark button
const updateHeaderBookmarkButton = function () {
  const headerBookmarkBtn = document.querySelector(".topbar__bookmark-btn");
  const bookmarkIcon = headerBookmarkBtn.querySelector(
    ".topbar__bookmark-icon use"
  );

  if (state.recipe.bookmarked) {
    bookmarkIcon.setAttribute("href", `${icons}#icon-bookmark-fill`);
  } else {
    bookmarkIcon.setAttribute("href", `${icons}#icon-bookmark`);
  }
};

// Controller function to add bookmark handler
const addHandlerBookmark = function (handler) {
  recipeDetailsDiv.addEventListener("click", function (e) {
    const btn = e.target.closest(".btn--bookmark");
    if (!btn) return;
    handler();
  });
};

// View function to generate recipe markup
const generateMarkup = function (recipe) {
  const ingredientsMarkup = recipe.ingredients
    .map(
      (ing) => `
    <li class="recipe__ingredient">
      <svg class="recipe__icon">
        <use href="${icons}#icon-check"></use>
      </svg>
      <div class="recipe__quantity">${ing.quantity || ""}</div>
      <div class="recipe__description">
        <span class="recipe__unit">${ing.unit || ""}</span>
        ${ing.description}
      </div>
    </li>
  `
    )
    .join("");

  return `
    <figure class="recipe__fig">
      <img src="${recipe.image}" alt="${recipe.title}" class="recipe__img" />
      <h1 class="recipe__title">
        <span>${recipe.title}</span>
      </h1>
    </figure>

    <div class="recipe__details">
      <div class="recipe__info">
        <svg class="recipe__info-icon">
          <use href="${icons}#icon-clock"></use>
        </svg>
        <span class="recipe__info-data recipe__info-data--minutes">${
          recipe.cookingTime
        }</span>
        <span class="recipe__info-text">minutes</span>
      </div>
      <div class="recipe__info">
        <svg class="recipe__info-icon">
          <use href="${icons}#icon-users"></use>
        </svg>
        <span class="recipe__info-data recipe__info-data--people">${
          recipe.servings
        }</span>
        <span class="recipe__info-text">servings</span>

        <div class="recipe__info-buttons">
          <button class="btn--tiny btn--decrease-servings">
            <svg>
              <use href="${icons}#icon-minus-circle"></use>
            </svg>
          </button>
          <button class="btn--tiny btn--increase-servings">
            <svg>
              <use href="${icons}#icon-plus-circle"></use>
            </svg>
          </button>
        </div>
      </div>

      <div class="recipe__user-generated ${recipe.key ? "" : "hidden"}">
        <svg>
          <use href="${icons}#icon-user"></use>
        </svg>
      </div>
      <button class="btn--round btn--bookmark">
        <svg class="">
          <use href="${icons}#icon-bookmark${
    recipe.bookmarked ? "-fill" : ""
  }"></use>
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
        This recipe was carefully designed and tested by
        <span class="recipe__publisher">${
          recipe.publisher
        }</span>. Please check out
        directions at their website.
      </p>
      <a
        class="btn--small recipe__btn"
        href="${recipe.sourceUrl}"
        target="_blank"
      >
        <span>Directions</span>
        <svg class="search__icon">
          <use href="${icons}#icon-arrow-right"></use>
        </svg>
      </a>
    </div>
  `;
};

// View function to generate search results markup
const generateSearchMarkup = function (recipes) {
  return recipes
    .map(
      (rec) => `
    <li class="preview">
      <a class="preview__link" href="#${rec.id}" data-id="${rec.id}">
        <figure class="preview__fig">
          <img src="${rec.image}" alt="${rec.title}" />
        </figure>
        <div class="preview__data">
          <h4 class="preview__title">${rec.title}</h4>
          <p class="preview__publisher">${rec.publisher}</p>
        </div>
      </a>
    </li>
  `
    )
    .join("");
};

// View function to display pagination
function displayPagination(page) {
  const numPages = Math.ceil(
    state.search.results.length / state.search.resultsPerPage
  );

  if (numPages <= 1) {
    document.querySelector(".pagination").innerHTML = ""; // No pagination needed
    return;
  }

  const generateButtonMarkup = (type, page) => `
    <button class="btn--inline pagination__btn--${type}" data-page="${page}">
      <svg class="search__icon">
        <use href="${icons}#icon-arrow-${
    type === "prev" ? "left" : "right"
  }"></use>
      </svg>
      <span>Page ${page}</span>
    </button>
  `;

  let markup = "";
  if (page > 1) {
    markup += generateButtonMarkup("prev", page - 1);
  }
  if (page < numPages) {
    markup += generateButtonMarkup("next", page + 1);
  }

  document.querySelector(".pagination").innerHTML = markup;

  // Add event listeners to pagination buttons
  document
    .querySelectorAll(".pagination__btn--prev, .pagination__btn--next")
    .forEach((button) => {
      button.addEventListener("click", (e) => {
        const targetPage = +e.target
          .closest("button")
          .getAttribute("data-page");
        state.search.page = targetPage;
        displaySearchResults(getSearchResultsPage(targetPage));
      });
    });
}
