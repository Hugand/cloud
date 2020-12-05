
class API {
    static async deleteFile(fileDirToDelete) {
        return await fetch(`${process.env.REACT_APP_API_URL}/delete/${encodeURIComponent(fileDirToDelete)}`, {
            method: "DELETE"
        })
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

        console.log(reqBody)

        return fetch(`${process.env.REACT_APP_API_URL}/rename`, {
            method: "PUT",
            body: reqBody
        })
    }

    static submitNewFile(reqBody) {
        return fetch(`${process.env.REACT_APP_API_URL}/upload`, {
            method: "POST",
            body: reqBody
        })
        .then(res => res.json())
    }

}

export default API