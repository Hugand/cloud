import React from 'react'
import { getFileSize } from '../../helpers/file'
import '../../styles/blocks/file-type-storage-list.scss'

/*
    @props {Object} selectedFileActions
*/
function FileTypeStorageList({ storageData }) {
    return (
        <div className="file-type-desc">
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Image</label>
                <label className="space-used light-text">{ getFileSize(storageData.storageInImages) }</label>
            </div>
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Videos</label>
                <label className="space-used light-text">{ getFileSize(storageData.storageInVideos) }</label>
            </div>
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Audio</label>
                <label className="space-used light-text">{ getFileSize(storageData.storageInAudio) }</label>
            </div>
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Docs</label>
                <label className="space-used light-text">{ getFileSize(storageData.storageInDocs) }</label>
            </div>
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Others</label>
                <label className="space-used light-text">{ getFileSize(storageData.storageInOthers) }</label>
            </div>
            <div className="file">
                <div className="file-type-icon"></div>
                <label className="file-type dark-text">Available</label>
                <label className="space-used light-text">{ getFileSize(storageData.currentAvailableSpace) }</label>
            </div>
        </div>
    )
}

export default FileTypeStorageList