import React, { useEffect, useState } from 'react'
import '../../styles/atoms/toast.scss'

/*
    @props {boolean} isVisible
    @props {Function} resetToast
    @props {String} msg
    @props {String} icon - iconPath
*/
function Toast(props) {
    const [ waitingForTimeout, setWaitingForTimeout ] = useState(false)
    useEffect(() => {
        if(props.isVisible && !waitingForTimeout){
            setWaitingForTimeout(true)
            setTimeout(() =>{
                setWaitingForTimeout(false)
                props.resetToast()
            }, 2000)
        }
    }, [props.isVisible])

    return (
        <div
            style={{
                right: props.isVisible ? '1rem' : '-20vw'
            }}
            className={"toast-container " }>
            { props.icon !== undefined && props.icon !== null && props.icon !== ""
             && <img className="toast-icon" src={'./assets/icons/' + props.icon} alt='icon'/>}
            <p className="toast-msg">{ props.msg }</p>
        </div>
    )
}

export default Toast
