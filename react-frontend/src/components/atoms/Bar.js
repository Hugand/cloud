import React from 'react'

/*
    @props {Array} displayData
*/
function Bar({ displayData }) {

    // if()
    // return <div className="bar space-bar">
    //     <div className={'space-filler curr-space'}
    //         style={{width: getPercentageFromBytes(
    //             storageData.currentUsedSpace,
    //             storageData.maxAvailableSpace
    //         ) + '%'}}></div>
    // </div>

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