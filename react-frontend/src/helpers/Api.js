
class API {
    JSON_HEADERS = {
        'Content-Type': 'application/json'
    }

    static async deleteFile(fileDirToDelete) {
        return await fetch(`${process.env.REACT_APP_API_URL}/delete/${encodeURIComponent(fileDirToDelete)}`, {
            method: "DELETE"
        })
        .then(res => res.json())
    }

    static async createDir(dirPathName) {
        return await fetch(`${process.env.REACT_APP_API_URL}/mkdir`, {
            method: "POST",
            body: JSON.stringify({ dirPathName })
        })
        .then(res => res.json())
    }

    static changeDir(socket, isConnected, dir, prevDir) {
        if (isConnected) {
          const data = {
              type: "cd",
              directory: dir.join('/') + '/',
              prevDir: prevDir.join('/') + '/'
          }
          socket.send(JSON.stringify(data));
        }
    }

    static async moveFile (fileName, currDir, newDir) {
        const reqBody = new FormData()
        reqBody.append("fileName", encodeURIComponent(fileName))
        reqBody.append("currDir", encodeURIComponent(currDir))
        reqBody.append("newDir", encodeURIComponent(newDir))

        return fetch(`${process.env.REACT_APP_API_URL}/move`, {
            method: "PUT",
            body: reqBody
        }).then(res => res.json())
    }

    static async renameFile (fileDir, prevName, newName) {
        const reqBody = new FormData()
        reqBody.append("fileDir", encodeURIComponent(fileDir))
        reqBody.append("prevName", encodeURIComponent(prevName))
        reqBody.append("newName", encodeURIComponent(newName))

        return fetch(`${process.env.REACT_APP_API_URL}/rename`, {
            method: "PUT",
            body: reqBody
        }).then(res => res.json())
    }

    static submitNewFile(reqBody) {
        return fetch(`${process.env.REACT_APP_API_URL}/upload`, {
            method: "POST",
            body: reqBody
        }).then(res => res.json())
    }

    /*
        @param {String} dirToList, example: dir1/dir2/dir3
    */
    static getFoldersInDir(dirToList) {
        return fetch(`${process.env.REACT_APP_API_URL}/getFoldersInDir/${encodeURIComponent(dirToList)}`)
            .then(res => res.json())
    }

}

export default API