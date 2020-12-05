
export const initialState = {
    dirs: [],
    socket: null,
    isConnected: false,
    toast: {
        isVisible: false,
        msg: '',
        icon: ''
    },
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
        case 'changeToastIsVisible':
            return {
                ...state,
                toast: {
                    ...state.toast,
                    isVisible: action.value
                }
            }
        case 'changeToastMsg':
            return {
                ...state,
                toast: {
                    ...state.toast,
                    msg: action.value
                }
            }
        case 'changeToastIcon':
            return {
                ...state,
                toast: {
                    ...state.toast,
                    icon: action.value
                }
            }
            
        default:
            return state
    }
}