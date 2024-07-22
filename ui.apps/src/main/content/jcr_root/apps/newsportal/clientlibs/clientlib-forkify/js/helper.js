const timeout = function (s) {
  return new Promise(function (_, reject) {
    setTimeout(function () {
      reject(new Error(`request took too long timeout after ${s} seconds`));
    }, s * 1000);
  });
};

export async function getJSON(url) {
  try {
    const response = await fetch(url);
    const data = response.json();
    return data;
  } catch (error) {
    console.error("There was a problem with the fetch operation:", error);
    throw error;
  }
}
