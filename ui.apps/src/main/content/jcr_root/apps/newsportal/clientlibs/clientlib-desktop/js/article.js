fetch("/bin/newsportal/articles")
  .then((response) => response.json())
  .then((data) => {
    const articleDetailsElement = document.getElementById("article-details");
    articleDetailsElement.textContent = JSON.stringify(data, null, 2);
  })
  .catch((error) => {
    console.error("Error fetching article details:", error);
    const articleDetailsElement = document.getElementById("article-details");
    articleDetailsElement.textContent = "Error fetching article details";
  });
