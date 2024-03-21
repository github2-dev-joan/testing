

    
   const actualTableHeaderId = ("actual-head");
   const historyTableHeaderId = ("history-head");
   const actualTableBodyId = ("actual-body");
   const historyTableBodyId = ("history-body");


    
   async function fetchData(){
    try{
        console.log("here 1");
        const response = await fetch(`http://localhost:8080/tregjet/getActualTable`);

        if(!response.ok){
            throw new Error("Could not fetch resource");
        }

        const data = await response.json();
        console.log(data);

        showTableHeader(actualTableHeaderId);
        hideTableHeader(historyTableHeaderId);
        displayActual(data);
        console.log("here 2");

    }
    catch (error){
        console.error(error);
    }
}

fetchData();




   function actualEvent(){
        //delete previous table before displaying new one
        deleteTable(actualTableBodyId);
        deleteTable(historyTableBodyId);
        fetchData();
    }   




    async function queryHistory (date){
        try{
            console.log("here 5");

            const parsedDate = new Date(date);
            console.log(parsedDate);
            const day = parsedDate.getDate();
            console.log(day);
            const month = parsedDate.getMonth()+1;
            console.log(month);
            const year = parsedDate.getFullYear();
            console.log(year);

            const formattedDate = `${year}-${month < 10 ? '0':''}${month}-${day < 10 ? '0' : ''}${day}`;
            console.log("formatted date: ",formattedDate);

            const response = await fetch(`http://localhost:8080/tregjet/getHistoryTableByDate?date=${formattedDate}`);
            if(!response.ok){
                console.error(`Failed to fetch resource. Status: ${response.status}`);
                const errorMessage = await response.text();
                console.error(`Server Response: ${errorMessage}`);
                throw new Error("Could not fetch resource");
            }

            const data = await response.json();

            console.log("here 6");
            console.log(data);

            //delete previous table before displaying new one
            showTableHeader(historyTableHeaderId);
            hideTableHeader(actualTableHeaderId);
            deleteTable(actualTableBodyId);
            deleteTable(historyTableBodyId);

            displayHistory(data);

        }catch (error){
            console.error(error);
        }
    }



    console.log("here 4");


    function findRates(){

        const date = document.getElementById("history_date").value;
        console.log(date);
        queryHistory(date);
    }


function displayActual(jsonData){

        if(!jsonData){
            console.error('No data to display');
            return;
        }

        const tableBody = document.getElementById('actual-body');

        console.log("before foreach");
        jsonData.forEach(item => {
            const row = document.createElement('tr');
            

            const cell1 = document.createElement('td');
            cell1.textContent = item.currency;
            row.appendChild(cell1);

            const cell2 = document.createElement('td');
            cell2.textContent = item.currencyAcronym;
            row.appendChild(cell2);

            const cell3 = document.createElement('td');
            cell3.textContent = item.exchangeRate;
            row.appendChild(cell3);

            const cell4 = document.createElement('td');
            cell4.textContent = item.difference;
            row.appendChild(cell4);

            const cell5 = document.createElement('td');
            cell5.textContent = item.publishedDate;
            row.appendChild(cell5);

            const cell6 = document.createElement('td');
            cell6.textContent = item.revisionNo;
            row.appendChild(cell6);

            tableBody.appendChild(row);
        });
    

}

console.log("here 3");


function displayHistory(jsonData){
    if(!jsonData){
        console.error('No data to display');
        return;
    }

    const tableBody = document.getElementById('history-body');

    console.log("before foreach");
    jsonData.content.forEach(item => {
        const row = document.createElement('tr');
        

        const cell1 = document.createElement('td');
        cell1.textContent = item.currency;
        row.appendChild(cell1);

        const cell2 = document.createElement('td');
        cell2.textContent = item.currencyAcronym;
        row.appendChild(cell2);

        const cell3 = document.createElement('td');
        cell3.textContent = item.exchangeRate;
        row.appendChild(cell3);

        const cell4 = document.createElement('td');
        cell4.textContent = item.difference;
        row.appendChild(cell4);

        const cell5 = document.createElement('td');
        cell5.textContent = item.publishedDate;
        row.appendChild(cell5);

        const cell6 = document.createElement('td');
        cell6.textContent = item.revisionNo;
        row.appendChild(cell6);

        tableBody.appendChild(row);
    });



}



function deleteTable(tableId){
    const tableBody = document.getElementById(tableId);
    tableBody.innerHTML = '';
}


function hideTableHeader(tableId){
    const myTable = document.getElementById(tableId);
    if(myTable){
        myTable.style.display = 'none';
    }
}


function showTableHeader(tableId){
    const myTable = document.getElementById(tableId);
    if(myTable){
        myTable.style.display = '';
    }
}







