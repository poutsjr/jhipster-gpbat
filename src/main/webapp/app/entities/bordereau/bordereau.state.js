(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bordereau', {
            parent: 'entity',
            url: '/bordereau?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.bordereau.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bordereau/bordereaus.html',
                    controller: 'BordereauController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bordereau');
                    $translatePartialLoader.addPart('uniteMetrique');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bordereau-detail', {
            parent: 'entity',
            url: '/bordereau/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.bordereau.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bordereau/bordereau-detail.html',
                    controller: 'BordereauDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bordereau');
                    $translatePartialLoader.addPart('uniteMetrique');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bordereau', function($stateParams, Bordereau) {
                    return Bordereau.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bordereau',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bordereau-detail.edit', {
            parent: 'bordereau-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bordereau/bordereau-dialog.html',
                    controller: 'BordereauDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bordereau', function(Bordereau) {
                            return Bordereau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bordereau.new', {
            parent: 'bordereau',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bordereau/bordereau-dialog.html',
                    controller: 'BordereauDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                article: null,
                                libelle: null,
                                unite: null,
                                prix: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bordereau', null, { reload: 'bordereau' });
                }, function() {
                    $state.go('bordereau');
                });
            }]
        })
        .state('bordereau.edit', {
            parent: 'bordereau',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bordereau/bordereau-dialog.html',
                    controller: 'BordereauDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bordereau', function(Bordereau) {
                            return Bordereau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bordereau', null, { reload: 'bordereau' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bordereau.delete', {
            parent: 'bordereau',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bordereau/bordereau-delete-dialog.html',
                    controller: 'BordereauDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bordereau', function(Bordereau) {
                            return Bordereau.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bordereau', null, { reload: 'bordereau' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
