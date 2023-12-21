document.querySelector("#bar-menu").onclick = () => {
  const navBar = document.querySelector(".navbar");
  const cartItem = document.querySelector(".cart-items-container");
  const searchForm = document.querySelector(".search-form");

  navBar.classList.toggle("active");
  cartItem.classList.remove("active");
  searchForm.classList.remove("active");
};
// function toggleNav() {
//   const navBar = document.querySelector(".navbar");
//   navBar.classList.toggle("active");
// }
document.querySelector("#cart-btn").onclick = () => {
  const navBar = document.querySelector(".navbar");
  const searchForm = document.querySelector(".search-form");
  const cartItem = document.querySelector(".cart-items-container");

  cartItem.classList.toggle("active");
  navBar.classList.remove("active");
  searchForm.classList.remove("active");
};

document.querySelector("#search-btn").onclick = () => {
  const navBar = document.querySelector(".navbar");
  const cartItem = document.querySelector(".cart-items-container");
  const searchForm = document.querySelector(".search-form");

  searchForm.classList.toggle("active");
  navBar.classList.remove("active");
  cartItem.classList.remove("active");
};

window.onscroll = () => {
  navBar.classList.remove("active");
  cartItem.classList.remove("active");
  searchForm.classList.remove("active");
};

function product() {
  alert("coming soon!");
}
