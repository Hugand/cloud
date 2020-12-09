import React from 'react'
import '../../styles/atoms/bar.scss'

/*
    @props {Array} displayData
*/
function Bar({ displayData }) {
    return (
        <div className="bar file-type-space-bar">
            { displayData.length > 0 &&
                displayData.map(item => 
                    <div key={item.class}
                        className={'space-filler ' + item.class}
                        style={{width: item.sizePercentage + '%'}}></div>
                ) }
        </div>
    )
}

export default Bar