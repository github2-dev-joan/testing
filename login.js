function userLogin(){
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    console.log("check 1");

    fetch("http://localhost:8080/tregjet/authenticate",{
        method: "POST", 
        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify({username: username, password: password})
    })
    .then(response => {
        if(response.ok){
            //provide main website path to ridirect users after authentication
            console.log("success");
            window.location.href = "http://127.0.0.1:5500/webscraper.html";
        }else{
            console.log("failure");
            document.getElementById("error").innerText = "Invalid username or password";
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}