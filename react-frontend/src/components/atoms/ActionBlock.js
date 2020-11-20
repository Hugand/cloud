import React from 'react'
import '../../styles/atoms/action-block.scss'

/*
* @props {string} label
* @props {img} icon
* @props {function} clickHandler
*/
function ActionBlock(props) {
    return (
        <div className="action-block" onClick={props.clickHandler}>
            <img className="icon" src={props.icon} alt="icon" />
            <label>{props.label}</label>
        </div>
    )
}

export default ActionBlock