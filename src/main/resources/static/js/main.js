(function () {

    let last_message_id = -1;
    window.addEventListener('DOMContentLoaded', (event) => {



        document.getElementById("add-message").addEventListener("submit", (e) => {
            e.preventDefault();

            let formData = new FormData();
            formData.append("message", document.getElementById("message").value);
            formData.append("username", document.getElementById("username").innerText);
            let jsonData = Object.fromEntries(formData.entries());
            console.log(document.getElementById("username").innerText);
            fetch('/api/text/add-message',
                {
                    method: 'POST',
                     headers: {
                         'Accept': 'application/json',
                         'Content-type': 'application/json',
                     },
                    body: JSON.stringify(jsonData),
                })
                .then(status)
                .then((response) => {
                    if (response.redirected) {
                        window.location.href = response.url;
                    }

                fetchLastMessages();



                }).catch(function (error) {
                console.log('Request failed', error);

            });
        })

        fetchLastMessages();
        fetchActiveUsers();
        setInterval(fetchActiveUsers, 10000);
        setInterval(fetchLastMessages, 10000)

    });

    function fetchActiveUsers() {
        let formData = new FormData();
        formData.append("username", document.getElementById("username").innerText);
        fetch('/api/active-users',
            {
                method: 'POST', redirect: 'follow',
                headers: {
                    'Accept': 'application/json',
                },
                body: formData
            })
            .then(status)
            .then((response) => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
                return response.json()
            })
            .then(function (users) {
                console.log(users);
                let html = "";

                for (let user of users) {

                    html += `<li class="list-group-item">${user}</li>`;
                    //console.log(html)
                }
                document.getElementById("active-users").innerHTML = html;



            }).catch(function (error) {
            console.log('Request failed', error);

        });
    }

    function fetchLastMessages() {
        let formData = new FormData();
        formData.append("lastID", last_message_id.toString());
        let jsonData = Object.fromEntries(formData.entries());
        fetch('/api/last-messages',
            {
                method: 'POST', redirect: 'follow',
                headers: {
                    'Accept': 'application/json',
                },
                body: formData,
            })
            .then(status)
            .then((response) => {
                if (response.redirected) {
                    window.location.href = response.url;
                }
                return response.json()
            })
            .then(function (messages) {
                console.log(messages);
                let html = "";

                if(messages.length == 0) {
                    console.log("no new message")
                    return;
                }
                last_message_id = messages[0].id;

                for (let msg of messages) {

                    html += `<li class="list-group-item">${msg.username}: ${msg.message}</li>`;
                    //console.log(html)
                }
                document.getElementById("last-messages").innerHTML = html;



            }).catch(function (error) {
            console.log('Request failed', error);

        });
    }
})();