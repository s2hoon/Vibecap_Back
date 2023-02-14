/**global variables */
/************************************************************/
/**colors */
const COLOR_MOUSE_DOWN = '#E5E7EA';
const COLOR_MOUSE_UP = '#FFFFFF';
const COLOR_MY_WALL = 'gray';

/** button */
const BUTTON_START_ID = "button-start";
const BUTTON_NEW_ID = "button-new";
const BUTTON_HINT_ID = "button-hint";
const BUTTON_SOLUTION_ID = "button-solution"

/** cell values */
const VIRUS_STRING = "🦠";      //  ui
const CELL_VALUE_NULL = -1;     // null
const CELL_VALUE_EMPTY = 0;
const CELL_VALUE_WALL = 1;
const CELL_VALUE_VIRUS = 2;

const ANSWER_LENGTH = 3;

/** game state */
const NOT_STARTED = 0
const PLAYING = 1
let GAME_STATE = NOT_STARTED;

const IMAGE_PATH = "./big-data.jpeg";

let N = 5;              // 연구실의 가로 세로 길이
let labMap = [];        // 연구실을 표현한 그래프
let wall = [];          // 벽의 위치 (0~(N-1) 정수)
let wallCoords = []     // 2차원 좌표에서의 벽의 위치
let answer = []         // 정답(벽의 좌표 세 개)
let answerIndex = []    // 정답을 정수 인덱스로 변환
let playerAnswer = []   // 사용자가 입력한 답
let answerVirus = []    // 정답일때의 바이러스가 퍼진 좌표
let virus = [];         // 바이러스의 위치 (0~(N-1) 정수)
let maxSafety = 0       // 안전 영역 최대값
let count = 0           // 탐색한 경우의 수
let etime = 0           // 정답 계산에 걸린 시간
/************************************************************/

/************************* UI *******************************/
/**
 * 버튼을 눌렀을 때의 반응
 * @param {DOM element} button
 */
function buttonMouseDown(button) {
    const mouseDownStyle = `background-color: ${COLOR_MOUSE_DOWN};
    border-left: 4px solid black;
    border-top: 4px solid black;
    border-right: 1px solid black;
    border-bottom: 1px solid black;`
    button.setAttribute("style", mouseDownStyle);
}

// TODO regex 공부
function selectCell(event) {
    const cell = event.target;
    const cellIndex = 1*cell.id.match(/\d+/)[0]
    const afterSelectedStyle = `background-color: ${COLOR_MY_WALL}; border: 1px solid black;`
    const beforeSelectedStyle = `background-color: white; border: 1px solid black;`

    let idx = -1;
    if ((idx = playerAnswer.indexOf(cellIndex)) > -1) {
        cell.setAttribute("style", beforeSelectedStyle)
        // playerAnswer = playerAnswer.filter((idx) => {1*idx!=cellIndex})
        playerAnswer.splice(idx, 1);
    } else if (playerAnswer.length >= ANSWER_LENGTH)
        alert(`벽은 ${ANSWER_LENGTH}개까지 설치할 수 있습니다!`)
    // 이미 선택한 셀인 경우 선택 취소
    // 아직 선택하지 않았던 셀인 경우 선택
    else { 
        cell.setAttribute("style", afterSelectedStyle)
        playerAnswer.push(cellIndex)
    }
    console.log(playerAnswer)

    // 정답 여부 판단
    let score = 0;
    if (playerAnswer.length == ANSWER_LENGTH) {
        for (const i of playerAnswer) {
            if (answerIndex.includes(i))
                score += 1;
        }
        if (score == ANSWER_LENGTH) 
            alert("정답입니다")
    }
}

/**
 * start부터 end-1까지의 임의의 정수 1개를 반환한다.
 * @param {int} start include   
 * @param {int} end   exclude
 */
function getRandomInteger(start, end) {
    return Math.floor(Math.random() * (end-start) + start);
}

/**
 * 버튼 반응 원상 복귀, 버튼 기능 실행
 * @param {DOM element} button
 */
function buttonMouseUp(button) {
    const mouseUpStyle = `background-color: ${COLOR_MOUSE_UP};
    border-left: 1px solid black;
    border-top: 1px solid black;
    border-right: 4px solid black;
    border-bottom: 4px solid black;`
    button.setAttribute("style", mouseUpStyle);

    const virusNum = getRandomInteger(2, 6)
    const wallNum = getRandomInteger(virusNum+2, virusNum+5)
    let start = 0;
    // TODO 하드코딩(5) 사용자로부터 입력받도록 수정
    // 게임 시작
    if (button.id == BUTTON_START_ID && GAME_STATE == NOT_STARTED) {
        start = new Date().getTime();
        do 
            initGameScreen(5, virusNum, wallNum);      // 이미 게임이 시작되었다면 무시
        while (combination(0) == 0)
        etime = (new Date().getTime() - start);                   // 정답을 구하는데 걸린 시간 (ms)
    // 새로운 게임 시작
    } else if (button.id == BUTTON_NEW_ID && GAME_STATE == PLAYING) {  // 새 게임 
        clearSolutionInfo()
        start = new Date().getTime();
        do
            initGameScreen(5, virusNum, wallNum);      // 아직 게임이 시작되지 않았다면 무시
        while (combination(0) == 0)
        etime = (new Date().getTime() - start);                   // 정답을 구하는데 걸린 시간 (ms)
    // 정답 보기
    } else if (button.id == BUTTON_SOLUTION_ID && GAME_STATE == PLAYING) {
        paintSolution();
    // 힌트 보기
    } else if (button.id == BUTTON_HINT_ID && GAME_STATE == PLAYING) {
        console.log(`안전 영역 넓이가 ${maxSafety} 되도록 벽을 설치하세요`)
    }
    console.log(answerIndex)
}

/**
 * game-screen 초기화
 * @param {int} n 연구실의 가로 세로 길이
 * @param {int} v 바이러스 개수
 * @param {int} w 벽 개수
 */
function initGameScreen(n, v, w) {
    resetGame();
    GAME_STATE = PLAYING;
    N = n;
    setWallCoords(w)
    setVirusCoords(v)
    createLabCell(N);
    drawObjects();
    initGraph();
    combination(0);
}

/**
 * game-screen에 n*n 개의 cell 생성
 * @param {int} n 연구실 가로 세로 크기
 */
function createLabCell(n) {
    const gameScreen = document.getElementById("game-screen");
    gameScreen.innerHTML = "";
    for (let i=0; i<(N*N); i++) {
        const cell = `<div class="labCell" id="labCell${i}"></div>`
        gameScreen.innerHTML += cell;
    }
}

/**
 * 벽, 바이러스를 그린다.
 */
function drawObjects() {
    const wallStyle = "background-color: black;";
    const virusStyle = "font-size: 2.4em; padding-top: 10%;";
    const cellStyle = "border: 1px dotted black;"

    for (let i=0; i<(N*N); i++) {
        const query = `div#labCell${i}`
        const gameScreenLabCell = document.querySelector(query)
        if (wall.includes(i))
            gameScreenLabCell.setAttribute("style", wallStyle);
        else if (virus.includes(i)) {
            gameScreenLabCell.innerText = VIRUS_STRING;
            gameScreenLabCell.setAttribute("style", virusStyle + cellStyle);
        } else {
            gameScreenLabCell.setAttribute("style", cellStyle);
            gameScreenLabCell.classList.add("empty")
            gameScreenLabCell.addEventListener("click", selectCell)
        }
    }
}

/**
 * 연구실을 그래프 형태로 표현(전역변수 lab을 초기화)한다.
 */
function initGraph() {
    for (let x=0; x<N; x++) {
        let row = [];
        for (let y=0; y<N; y++)
            row.push(CELL_VALUE_NULL);
        labMap.push(row);
    }       
    for (let i=0; i<N*N; i++) {
        const x = Math.trunc(i/N);
        const y = i%N;
        if (wall.includes(i)) {
            labMap[x][y] = CELL_VALUE_WALL;
            // wallCoords.push([x,y]);
        }
        else if (virus.includes(i))
            labMap[x][y] = CELL_VALUE_VIRUS;
        else 
            labMap[x][y] = CELL_VALUE_EMPTY;
    }
}

/**
 * 연구실 한 칸을 표현
 */
class LabMaplabMapCell {
    /**
     * cell의 x,y 좌표 초기화
     * @param {int} x 
     * @param {int} y 
     * @param {int} obj 빈 공간: 0, 벽: 1, 바이러스: 2 
     */
    constructor(x, y, obj) {
        this.x = x;
        this.y = y;
        this.obj = obj;
    }
}

/**
 * n개의 벽(또는 바이러스)의 위치를 list<int> 형태로 반환한다.
 * @param {int} n 생성할 벽의 개수
 */
function setWallCoords(n) {
    while (wall.length!=n) {
        num = Math.floor(Math.random() * (N*N));
        if (!wall.includes(num) && !virus.includes(num))
            wall.push(num)
    }
}

/**
 * n개의 바이러스의 위치를 설정
 * @param {int} n 생성할 바이러스 개수
 */
function setVirusCoords(n) {
    while (virus.length!=n) {
        num = Math.floor(Math.random() * (N*N));
        if (!wall.includes(num) && !virus.includes(num))
            virus.push(num)
    }
}

/**
 * 기존에 존재하던 게임 정보를 삭제.
 * 바이러스, 벽, 연구실 가로 세로 길이 정보
 */
function resetGame() {
    GAME_STATE = NOT_STARTED;
    labMap = [];
    wall = [];
    wallCoords = [];
    virus = [];
    N = 0;
    count = 0;
    etime = 0;
    maxSafety = 0;
    answerVirus = [];
    playerAnswer = []
    answerIndex = []
}

/**
 * 정답 출력
 * answer 배열에 설치해야 할 벽의 좌표가 저장되어 있다.
 */
function paintSolution() {
    // 벽 표시
    const answerStyle = `background-color: ${COLOR_MY_WALL}`;
    let targetCell = []
    for (const coord of answer)
        targetCell.push(coord[0]*N + coord[1])
    for (let i of targetCell) {
        const query = `div#labCell${i}`
        const gameScreenLabCell = document.querySelector(query)
            gameScreenLabCell.setAttribute("style", answerStyle);
    }
    // 바이러스 확산 경로 표시
    const virusStyle = "font-size: 2.4em; padding-top: 10%; border: 1px dotted black;";
    targetCell = []
    for (const coord of answerVirus)
        targetCell.push(coord[0]*N + coord[1])
    for (let i of targetCell) {
        const query = `div#labCell${i}`
        const gameScreenLabCell = document.querySelector(query)
            gameScreenLabCell.innerText = VIRUS_STRING;
            gameScreenLabCell.setAttribute("style", virusStyle)
    }
    // solution-info 출력
    // 시간, 경우의 수, 최대 안전 영역 출력
    const timeInfo = `<div class="gcell-info" id="info-elapsed-time">걸린 시간 <br><br> ${etime}ms</div>`;
    const caseInfo = `<div class="gcell-info" id="info-case-number">계산한 경우의 수 <br><br> ${count}</div>`;
    const maxSafetyInfo = `<div class="gcell-info" id="info-maxSafety">안전 영역 최대값 <br><br> ${maxSafety}</div>`;
    const solutionInfoContainer = document.getElementById("solution-info");
    solutionInfoContainer.innerHTML = '';
    solutionInfoContainer.innerHTML += caseInfo;
    solutionInfoContainer.innerHTML += timeInfo;
    solutionInfoContainer.innerHTML += maxSafetyInfo;
    // 그림 표시
    const image = `<img id="info-ai-image" 
                        src=${IMAGE_PATH} 
                        width="100%"
                        alt="computer">`
    solutionInfoContainer.innerHTML += image;

}

function clearSolutionInfo() {
    const solutionInfo = document.getElementById("solution-info");
    solutionInfo.innerHTML = '';
}

/************************ 정답 계산 **************************/
const dx = [0, 1, 0, -1]
const dy = [1, 0, -1, 0]

function valid(graph, x, y) {
    return (x<N && x>-1 && y<N && y>-1 && graph[x][y]==0)
}

function spread(graph, sx, sy) {
    q=[]
    q.push([sx, sy])
    while (q.length != 0) {
        let [x, y] = q.shift()
        for (let i=0; i<4; i++) {
            let nextX = x + dx[i]
            let nextY = y + dy[i]
            if (!valid(graph, nextX, nextY))
                continue
            q.push([nextX, nextY])
            if (graph[x][y] == CELL_VALUE_VIRUS)
                graph[nextX][nextY] = CELL_VALUE_VIRUS
		}
	}
}

function countSafety(graph) {
    result = 0
    for (let x=0; x<N; x++) {
        for (let y=0; y<N; y++)
            if (graph[x][y] == CELL_VALUE_EMPTY)
                result += 1
	}
    return result
}

function findVIRUS(graph) {
    result = []
    for (let x=0; x<N; x++)
        for (let y=0; y<N; y++)
            if (graph[x][y] == CELL_VALUE_VIRUS)
                result.push([x, y])
    return result
}

function dcopy(dest, src) {
	for (let x=0; x<N; x++)
			for (let y=0; y<N; y++)
				dest[x][y] = src[x][y]
} 

/**
 * 
 * dfs 방식으로 벽을 세 개 설치하는 모든 경우의 수(₆₄C₃)에 대해 안전 영역 계산
 * @param {int} index 
 * @returns 
 */
function combination(index) {
    // console.log(`combination executing... ${count}`)
    // 벽 세 개 설치 후 바이러스가 퍼진 뒤 안전 영역 넓이 측정
    if (wallCoords.length == 3) {
        count += 1;
		// 연구실 원본 그래프 획득 (deep copy)
        let data = []
		for (let x=0; x<N; x++) {
			let row = []
			for (let y=0; y<N; y++)
				row.push(labMap[x][y])
			data.push(row)
		}
        for (const w of wallCoords) {
            data[w[0]][w[1]] = CELL_VALUE_WALL;
		}
        // 바이러스가 퍼지는 경우 시뮬레이션
        for (const v of findVIRUS(data)) {
            spread(data, v[0], v[1])
		}
        const safety = countSafety(data);
        // 최대값 업데이트
        if (safety > maxSafety) {
            // 바이러스가 퍼진 경로 저장
            answerVirus = []
            for (let x=0; x<N; x++) {
                for (let y=0; y<N; y++) {
                    if (data[x][y] == CELL_VALUE_VIRUS)
                        answerVirus.push([x,y])
                }

            }
            // 안전 영역 값 갱신
            maxSafety = safety;
            answer = [];
            answerIndex = [];
            for (const w of wallCoords) {
                answer.push(w);
                answerIndex.push(w[0]*N + w[1])
            }
        }
        return
	}
    else {
        for (let i=index; i<N*N; i++) {
            let x = Math.trunc(i/N)
			let y = i%N
            if (labMap[x][y] == CELL_VALUE_EMPTY) {
                wallCoords.push([x, y])
                combination(i+1)
                wallCoords.pop()
			}
            else
                continue    // 벽을 설치할 수 없는 공간
		}
	}
}

/********************** for debugging ********************/
function printGameStat() {
    const stat = `연구실 지도: ${labMap} \n
                 설치해야하는 벽: ${answer} \n
                 안전영역 최대 크기: ${maxSafety} \n
                 게임 상태: ${GAME_STATE} \n
                 탐색한 경우의 수: ${count}`

    console.log(stat)
}
/**
let N = 5;              // 연구실의 가로 세로 길이
let labMap = [];        // 연구실을 표현한 그래프
let wall = [];          // 벽의 위치 (0~(N-1) 정수)
let wallCoords = []     // 2차원 좌표에서의 벽의 위치
let answer = []         // 정답(벽의 좌표 세 개)
let virus = [];         // 바이러스의 위치 (0~(N-1) 정수)
let maxSafety = 0       // 안전 영역 최대값
let count = 0           // 탐색한 경우의 수
**/