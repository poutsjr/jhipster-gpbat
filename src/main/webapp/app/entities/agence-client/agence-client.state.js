(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agence-client', {
            parent: 'entity',
            url: '/agence-client?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.agenceClient.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agence-client/agence-clients.html',
                    controller: 'AgenceClientController',
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
                    $translatePartialLoader.addPart('agenceClient');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agence-client-detail', {
            parent: 'entity',
            url: '/agence-client/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.agenceClient.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agence-client/agence-client-detail.html',
                    controller: 'AgenceClientDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agenceClient');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgenceClient', function($stateParams, AgenceClient) {
                    return AgenceClient.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agence-client',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agence-client-detail.edit', {
            parent: 'agence-client-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agence-client/agence-client-dialog.html',
                    controller: 'AgenceClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgenceClient', function(AgenceClient) {
                            return AgenceClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agence-client.new', {
            parent: 'agence-client',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agence-client/agence-client-dialog.html',
                    controller: 'AgenceClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                secteur: null,
                                adresse: null,
                                chefAgence: null,
                                chefService: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('agence-client', null, { reload: 'agence-client' });
                }, function() {
                    $state.go('agence-client');
                });
            }]
        })
        .state('agence-client.edit', {
            parent: 'agence-client',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agence-client/agence-client-dialog.html',
                    controller: 'AgenceClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgenceClient', function(AgenceClient) {
                            return AgenceClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agence-client', null, { reload: 'agence-client' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agence-client.delete', {
            parent: 'agence-client',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agence-client/agence-client-delete-dialog.html',
                    controller: 'AgenceClientDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgenceClient', function(AgenceClient) {
                            return AgenceClient.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agence-client', null, { reload: 'agence-client' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
