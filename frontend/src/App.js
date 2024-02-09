import React from 'react';
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import {AuthProvider, useAuth} from './AuthContext';
import IndexPage from './components/home/IndexPage';
import Header from './components/header/Header';
import MyPageHeader from './components/header/MyPageHeader';
import LoginPage from './components/member/LoginPage';
import SignUpPage from './components/member/SignUpPage';
import TotalBoard from './components/board/page/total/TotalBoard';
import WriteBoard from "./components/board/page/write/WriteBoard";
import MyUpdatePage from './components/member/MyUpdatePage';
import MyDeletePage from './components/member/MyDeletePage';
import AdminPage from "./components/admin/AdminPage";
import AdminHeader from "./components/admin/AdminHeader";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <div className="App">
                    <Routes>
                        {/* 메인 홈페이지 라우트 */}
                        <Route path="/" element={<WithHeader><IndexPage/></WithHeader>}/>
                        {/* 사용자 라우트 */}
                        <Route path="/login" element={<WithHeader><LoginPage/></WithHeader>}/>
                        <Route path="/signup" element={<WithHeader><SignUpPage/></WithHeader>}/>
                        <Route path="/profile/update" element={<WithHeader><MyUpdatePage/></WithHeader>}/>
                        <Route path="/profile/delete" element={<WithHeader><MyDeletePage/></WithHeader>}/>
                        <Route path="/community" element={<WithHeader><TotalBoard/></WithHeader>} />
                        <Route path="/write" element={<WithHeader><WriteBoard/></WithHeader>} />
                        {/* 관리자 라우트 */}
                        <Route path="/admin/page" element={<AdminPage />} />
                    </Routes>
                </div>
            </BrowserRouter>
        </AuthProvider>
    );
}

function WithHeader({children}) {
    return (
        <>
            <AuthComponent/>
            {children}
        </>
    );
}

function AuthComponent() {
    const {isLoggedIn, userRole} = useAuth();

    if (!isLoggedIn) {
        return <Header/>;
    } else {
        switch (userRole) {
            case 'ADMIN':
                return <AdminHeader/>; // 관리자용 헤더 컴포넌트
            case 'USER':
            default:
                return <MyPageHeader/>; // 일반 사용자용 헤더 컴포넌트
        }
    }
}

export default App;
