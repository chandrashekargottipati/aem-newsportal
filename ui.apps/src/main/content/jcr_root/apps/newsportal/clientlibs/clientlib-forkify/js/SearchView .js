// class SearchView {
//   #parentElement = document.querySelector(".results");

//   render(data) {
//     const markup = this.#generateMarkup(data);
//     this.#clear();
//     this.#parentElement.insertAdjacentHTML("afterbegin", markup);
//   }

//   renderSpinner() {
//     const markup = `
//         <div class="spinner">
//           <svg>
//             <use href="${icons}#icon-loader"></use>
//           </svg>
//         </div>
//       `;
//     this.#clear();
//     this.#parentElement.insertAdjacentHTML("afterbegin", markup);
//   }

//   renderError(message = "No results found for your query. Please try again!") {
//     const markup = `
//         <div class="error">
//           <div>
//             <svg>
//               <use href="${icons}#icon-alert-triangle"></use>
//             </svg>
//           </div>
//           <p>${message}</p>
//         </div>
//       `;
//     this.#clear();
//     this.#parentElement.insertAdjacentHTML("afterbegin", markup);
//   }

//   #clear() {
//     this.#parentElement.innerHTML = "";
//   }

//   #generateMarkup(data) {
//     return data
//       .map((result) => {
//         return `
//             <li class="preview">
//               <a class="preview__link" href="#${result.id}">
//                 <figure class="preview__fig">
//                   <img src="${result.image}" alt="${result.title}" />
//                 </figure>
//                 <div class="preview__data">
//                   <h4 class="preview__title">${result.title}</h4>
//                   <p class="preview__publisher">${result.publisher}</p>
//                   <div class="preview__user-generated hidden">
//                     <svg>
//                       <use href="${icons}#icon-user"></use>
//                     </svg>
//                   </div>
//                 </div>
//               </a>
//             </li>
//           `;
//       })
//       .join("");
//   }
// }
