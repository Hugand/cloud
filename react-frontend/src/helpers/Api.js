
class API {
    static async deleteFile(fileDirToDelete) {
        return await fetch(`${process.env.REACT_APP_API_URL}/delete/${encodeURIComponent(fileDirToDelete)}`, {
            method: "DELETE"
        })
        .then(res => res.json())
    }

    static changeDir(socket, isConnected, dir, prevDir) {
        if (isConnected) {
          const data = {
              type: "cd",
              directory: './' + dir.join('/'),
              prevDir: './' + prevDir.join('/')
          }
          socket.send(JSON.stringify(data));
        }
    }

    static async moveFile () {

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

}

export default API