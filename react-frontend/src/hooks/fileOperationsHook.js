import API from '../helpers/Api'
import { useStateValue } from '../state'

function useFileOperations() {
    const [ {dirs, socket, isConnected }, dispatch ] = useStateValue()

    const navigateToDir = dirName => {
        let newDirStack = [
        ...dirs,
        dirName
        ]
        const tmp = [...dirs]
        dispatch({
            type: 'changeDirs',
            value: newDirStack
        })
        API.changeDir(socket, isConnected, newDirStack, tmp)
    }

    const goBack = () => {
        let dirStack = [...dirs]
        const tmp = [...dirs]

        dirStack.pop()
        dispatch({
            type: 'changeDirs',
            value: dirStack
        })
        API.changeDir(socket, isConnected, dirStack, tmp)
    }

    const deleteFile = async (fileName) => {
        const fileDirToDelete = `${dirs.join('/')}/${fileName}`
        let res = await API.deleteFile(fileDirToDelete)
        const toastData = {
            isVisible: true,
            icon: 'error_icon.svg',
            msg: ''
        }

        switch(res.status) {
            case 'success':
                toastData.icon = 'success_icon.svg'
                toastData.msg = 'Success: File renamed!'
                break;
            case 'error':
                if(res.desc === 'PATH_INVALID'){
                    toastData.msg = `Error: The given path is invalid (${fileDirToDelete}`
                } else {
                    toastData.msg = `Error: Error deleting file (${fileDirToDelete}`
                }
                break;
            case 'failed':
            default:
                toastData.msg = `Error: Error deleting file (${fileDirToDelete}`
        }

        dispatch({
            type: 'changeToast',
            value: toastData
        })
    }

    const renameFile = async (prevName, newName) => {
        const fileDir = `${dirs.join('/')}/`
        const res = await API.renameFile(fileDir, prevName, newName)
        const toastData = {
            isVisible: true,
            icon: 'error_icon.svg',
            msg: ''
        }

        switch(res.status) {
            case 'success':
                toastData.icon = 'success_icon.svg'
                toastData.msg = 'Success: File renamed!'
                break;
            case 'error':
                switch(res.desc) {
                    case 'FILE_ALREADY_EXISTS':
                        toastData.msg = `Error: There's already a file named "${newName}"`
                        break
                    case 'FILE_DOESNT_EXIST':
                        toastData.msg = `Error: The file with the name "${prevName}" doesn't exit`
                        break
                    default:
                        toastData.msg = `Error: Error renaming file (${prevName}`
                }
                break;
            case 'failed':
            default:
                toastData.msg = `Error: Error renaming file (${prevName}`
        }
            
        dispatch({
            type: 'changeToast',
            value: toastData
        })
    }

    const getFoldersInDir = async (dirStack) => {
        const dirToList = dirStack.join('/') + '/'
        const res = await API.getFoldersInDir(dirToList)
        switch(res.status) {
            case 'success':
                return res.folderList
            case 'error':
            default:
                dispatch({
                    type: 'changeToast',
                    value: {
                        isVisible: true,
                        icon: 'error_icon.svg',
                        msg: "Error: Error retrieving folder's list"
                    }
                })
                return null
        }
    }

    return {
        navigateToDir,
        goBack,
        deleteFile,
        renameFile,
        getFoldersInDir
    }
}

export default useFileOperations