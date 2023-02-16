labMap = [[0,0,0,2,0],
		  [1,2,0,0,2], 
		  [0,0,0,0,0],
		  [2,1,1,0,0],
		  [1,1,0,0,2]]
n = 5;
m = 5;

VIRUS = 2
WALL = 1
FREE = 0

const dx = [0, 1, 0, -1]
const dy = [1, 0, -1, 0]

function valid(graph, x, y) {
    return (x<n && x>-1 && y<m && y>-1 && graph[x][y]==0)
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
            if (graph[x][y] == VIRUS)
                graph[nextX][nextY] = VIRUS
		}
	}
}

function countSafety(graph) {
    result = 0
    for (let x=0; x<n; x++) {
        for (let y=0; y<m; y++)
            if (graph[x][y] == FREE)
                result += 1
	}
    return result
}

function findVIRUS(graph) {
    result = []
    for (let x=0; x<n; x++)
        for (let y=0; y<m; y++)
            if (graph[x][y] == VIRUS)
                result.push([x, y])
    return result
}

function dcopy(dest, src) {
	for (let x=0; x<n; x++)
			for (let y=0; y<m; y++)
				dest[x][y] = src[x][y]
} 

wallCoords = []
maxSafety = 0
count = 0
// dfs 방식으로 벽을 세 개 설치하는 모든 경우의 수(₆₄C₃)에 대해 안전 영역 계산
function combination(index) {
    // 벽 세 개 설치 후 바이러스가 퍼진 뒤 안전 영역 넓이 측정
    if (wallCoords.length == 3) {
        count += 1;
		// 연구실 원본 그래프 획득
        let data = []
		for (let x=0; x<n; x++) {
			let row = []
			for (let y=0; y<m; y++)
				row.push(labMap[x][y])
			data.push(row)
		}
        // dcopy(data, labMap)		// deep copy
        for (const w of wallCoords) {
            data[w[0]][w[1]] = WALL;
		}
        for (const virus of findVIRUS(data)) {
			console.log(virus)
            spread(data, virus[0], virus[1])
		}
        maxSafety = Math.max(maxSafety, countSafety(data))
        return
	}
    else {
        // for i in range(index, n*m)
        for (let i=index; i<n*m; i++) {
            let x = Math.trunc(i/m)
			let y = i%m
            if (labMap[x][y] == FREE) {
                wallCoords.push([x, y])
                combination(i+1)
                wallCoords.pop()
			}
            else
                continue    // 벽을 설치할 수 없는 공간
		}
	}
}

combination(0)
console.log(maxSafety)
console.log(`고려한 경우의 수: ${count}`)
