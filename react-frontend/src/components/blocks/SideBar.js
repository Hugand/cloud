import React, { useEffect, useState } from 'react'
import { getFileSize } from '../../helpers/file'
import { useStateValue } from '../../state'
import '../../styles/blocks/sidebar.scss'
import Bar from '../atoms/Bar'
import FileActions from './FileActions'
import FileTypeStorageList from './FileTypeStorageList'

/*
    @props {Object} storageData
*/
function SideBar({ storageData }) {
    // Array of objects { class, sizePercentage }
    const [ storageBarDisplayData, setStorageBarDisplayData ] = useState([])
    const [ { selectedFileActions }, dispatch ] = useStateValue()
    
    useEffect(() => {
        const {
            currentUsedSpace,
            storageInImages,
            storageInVideos,
            storageInAudio,
            storageInDocs,
            storageInOthers } = storageData

        let barDisplayData = [
            { class: 'images', sizePercentage: getPercentageFromBytes(storageInImages, currentUsedSpace) },
            { class: 'videos', sizePercentage: getPercentageFromBytes(storageInVideos, currentUsedSpace) },
            { class: 'audio', sizePercentage: getPercentageFromBytes(storageInAudio, currentUsedSpace) },
            { class: 'docs', sizePercentage: getPercentageFromBytes(storageInDocs, currentUsedSpace) },
            { class: 'others', sizePercentage: getPercentageFromBytes(storageInOthers, currentUsedSpace) },
        ]

        barDisplayData = barDisplayData
            .sort((a, b) => a.sizePercentage < b.sizePercentage) // Sort values

        let accumulatedSum = 0;

        // Correct the sizePercentage to prevent bar overlaping
        barDisplayData = barDisplayData.map(fileType =>{
            accumulatedSum += fileType.sizePercentage

            return {
                ...fileType,
                sizePercentage: accumulatedSum
            }
        })
        .reverse() // Reverse so the smallest bar is the last one being rendered

        setStorageBarDisplayData(barDisplayData)
    }, [storageData])

    function getPercentageFromBytes(valInBytes, maxValInBytes) {
        return (valInBytes * 100) / maxValInBytes
    }

    return (
        <section className="sidebar">
            <section className="content">
                <div className="space—usage-display">
                    <div className="labels">
                        <h1>{ getFileSize(storageData.currentUsedSpace) }</h1>
                        <p>{ getFileSize(storageData.currentAvailableSpace) } available</p>
                    </div>
                    <Bar displayData={[{
                        class: 'curr-space',
                        sizePercentage: getPercentageFromBytes(
                            storageData.currentUsedSpace,
                            storageData.maxAvailableSpace
                        )
                    }]} />
                    
                    <Bar displayData={storageBarDisplayData} />
                
                </div>
                
                <FileTypeStorageList storageData={storageData} />

                { selectedFileActions !== null &&
                    <FileActions />
                }
            </section>
        </section>
    )
}

export default SideBar