// const baseUrl = "http://ec2-175-41-230-93.ap-northeast-1.compute.amazonaws.com:8080/wireframe";
const baseUrl = "http://localhost:8080/wireframe";
const api_capture = "/vibe/capture";
const api_capture_without_image = "/vibe/capture-without-image";
const api_capture_from_gallery = "/vibe/capture-from-gallery";

function handleButtonClick(e) {
    const checkedNodes = document.querySelectorAll('input[type="checkbox"]:checked');
    if (checkedNodes.length > 1 || checkedNodes.length == 0) {
        alert("호출할 api를 하나 선택해주세요")
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

function call(url) {
    let formData = new FormData();

    const extraInfo = document.querySelector('input[type="text"]').value;
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
        ulTag.innerHTML += `<li><a href=${response.video_list[i]}>recommend ${i}</a></li>`;
    }
}