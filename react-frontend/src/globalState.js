
export const initialState = {
    dirs: [],
    socket: null,
    isConnected: false,
    toast: {
        isVisible: false,
        msg: '',
        icon: ''
    },
    selectedFileActions: null,
    displayMoveFileModal: false
}

/*
    action: { type, value }
*/
export const reducer = (state, action) => {
    switch(action.type) {
        case 'changeDirs':
            return {
                ...state,
                dirs: action.value
            }
        case 'changeSocket':
            return {
                ...state,
                socket: action.value
            }
        case 'changeIsConnected':
            return {
                ...state,
                isConnected: action.value
            }
        case 'changeToast': 
            return {
                ...state,
                toast: action.value
            }
        case 'resetToast':
            return {
                ...state,
                toast: {
                    msg: '',
                    icon: '',
                    isVisible: false
                }
            }
        case 'changeSelectedFileActions':
            return {
                ...state,
                selectedFileActions: action.value
            }
        case 'changeDisplayMoveFileModal':
            return {
                ...state,
                displayMoveFileModal: action.value
            }
            
        default:
            return state
    }
}