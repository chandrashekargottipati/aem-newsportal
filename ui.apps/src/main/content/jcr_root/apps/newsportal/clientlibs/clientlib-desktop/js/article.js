/*document.addEventListener("DOMContentLoaded", function () {
            fetch('/bin/newsportal/articles')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok ' + response.statusText);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Fetched Articles:', data);
                    const container = document.getElementById('articles-container');
                    data.forEach(article => {
                        const articleDiv = document.createElement('div');
                        articleDiv.className = 'article';

                        const id = document.createElement('div');
                        id.className = 'article-id';
                        id.textContent = 'ID: ' + article.id;

                        const title = document.createElement('div');
                        title.className = 'article-title';
                        title.textContent = article.title;

                        const body = document.createElement('div');
                        body.className = 'article-body';
                        body.textContent = article.body;

                        articleDiv.appendChild(id);
                        articleDiv.appendChild(title);
                        articleDiv.appendChild(body);

                        container.appendChild(articleDiv);
                    });
                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation:', error);
                });
        });*/


  $(document).ready(function() {
            $('#createUserForm').submit(function(e) {
                e.preventDefault();

                $.ajax({
                    url: '/bin/newsportal/service/users',
                    type: 'POST',
                    data: $(this).serialize(),
                    success: function(response) {
                        $('#result').html('<p style="color: green;">User created successfully: ' + response + '</p>');
                        
                        // Clear form fields
                        $('#createUserForm')[0].reset();
                    },
                    error: function(xhr, status, error) {
                        $('#result').html('<p style="color: red;">Error creating user: ' + error + '</p>');
                    }
                });
            });
        });