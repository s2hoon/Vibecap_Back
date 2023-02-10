const baseUrl = "http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/wireframe";
// const baseUrl = "http://localhost:8080/wireframe";
const api_capture = "/vibe/capture";
const api_capture_without_image = "/vibe/capture-without-image";
const api_capture_from_gallery = "/vibe/capture-from-gallery";

const COLOR_MOUSE_DOWN = "#673676"
const COLOR_MOUSE_UP = "#BFAAF9"

let currentBodyColorIndex = 0;
const bodyColor = ["#F5C260", "#F5C26C", "#F58F6C", "#F6EB4E"];

/************************************ event handlers ************************************/

function handleButtonClick(e) {
    const checkedNodes = document.querySelectorAll('input[type="checkbox"]:checked');
    if (checkedNodes.length > 1 || checkedNodes.length == 0) {
        alert("추천 방식을 하나 선택해주세요")
        return
    }
    const checkedOption = checkedNodes[0].getAttribute("value")
    let apiUrl;
    switch(checkedOption) {
        case '0': apiUrl = baseUrl + api_capture_from_gallery;
            break;
        case '1': apiUrl = baseUrl + api_capture_without_image;
            break;
        case '2': apiUrl = baseUrl + api_capture;
            break;
    }

    const ulTag = document.getElementById('result-list');
    ulTag.innerHTML = '<h2> searching... </h2>'
    call(apiUrl)
}   

function onMouseDown(button) {
    const mouseDownStyle = `background-color: ${COLOR_MOUSE_DOWN};
                            border-left: 5px solid black;
                            border-top: 5px solid black;
                            border-right: 2px solid black;
                            border-bottom: 2px solid black;`
    button.setAttribute("style", mouseDownStyle);
}

function onMouseUp(button) {
    const mouseUpStyle = `background-color: ${COLOR_MOUSE_UP};
                            border-left: 2px solid black;
                            bordier-top: 2px solid black;
                            border-right: 5px solid black;
                            bordoer-bottom: 5px solid black;`
    button.setAttribute("style", mouseUpStyle);
}

function scrollBodyColor(e) {
    bodyTag = document.querySelector('body');
    currentBodyColorIndex = (currentBodyColorIndex+1) % (bodyColor.length)
    bodyTag.setAttribute("style", `background-color: ${bodyColor[currentBodyColorIndex]}`)
}

/****************************************************************************************/

/******************************* REST API ***********************************************/
function call(url) {
    let formData = new FormData();

    // TODO 수정
    // const extraInfo = document.querySelector('input[type="text"]').value;
    const imgFile = document.querySelector('input[type="file"]').files[0]; 

    if (url == baseUrl + api_capture) {
        formData.append('extra_info', extraInfo);
        formData.append('image_file', imgFile);
    } else if (url == baseUrl + api_capture_from_gallery) {
        formData.append('image_file', imgFile)
    } else if (url == baseUrl + api_capture_without_image) {
        formData.append('extra_info', extraInfo)
    }

    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .catch(error => alert('Error: ', error))
    .then(response => printResults(JSON.parse(JSON.stringify(response))))
}
/****************************************************************************************/

/**
 * response 형태
 * {
 *      "video_list" [
 *          "link1", "link2", "link3", ...
 *      ]
 * }
 */
function printResults(response) {
    if (response.video_list.length === 0) {
        alert("no video");
        return;
    }
    console.log(response.video_list)
    const ulTag = document.getElementById('result-list');
    ulTag.innerHTML ='';
    for (let i=0; i<response.video_list.length; i++) {
        ulTag.innerHTML += `<li class="recommendation-link"><a target="_blank" href=${response.video_list[i]}>recommend ${i}</a></li>`;
    }
}

function extractInformation() {
     
}

/***************************************** main *****************************************/
// window.addEventListener("scroll", scrollBodyColor);